package com.example.expensetrcaker

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetrcaker.ai.SpendingPersonality
import com.example.expensetrcaker.ui.theme.AccentGreen
import com.example.expensetrcaker.ui.theme.AccentPink
import com.example.expensetrcaker.ui.theme.BackgroundDark
import com.example.expensetrcaker.ui.theme.Primary
import com.example.expensetrcaker.ui.theme.Secondary
import com.example.expensetrcaker.ui.theme.SurfaceDark
import com.example.expensetrcaker.viewmodel.TransactionViewModel

@Composable
fun PersonalityCard(personality: SpendingPersonality) {
    Column(
            modifier =
                    Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                    Brush.linearGradient(
                                            listOf(
                                                    personality.color.copy(alpha = 0.15f),
                                                    Color.Transparent
                                            )
                                    )
                            )
                            .border(
                                    1.dp,
                                    personality.color.copy(alpha = 0.3f),
                                    RoundedCornerShape(24.dp)
                            )
                            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                    modifier =
                            Modifier.size(48.dp)
                                    .clip(CircleShape)
                                    .background(personality.color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
            ) { Icon(personality.icon, contentDescription = null, tint = personality.color) }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                        text = personality.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                )
                Text(
                        text = personality.description,
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
                text = personality.insight,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
        )
    }
}

@Composable
fun AnalyticsScreen(onBack: () -> Unit, viewModel: TransactionViewModel) {
    val transactions by viewModel.allTransactions.observeAsState(initial = emptyList())
    val totalSpent by viewModel.totalExpense.observeAsState(initial = 0.0)

    val stats =
            remember(transactions) {
                val expenseTransactions = transactions.filter { it.isExpense() }
                val categoryTotals =
                        expenseTransactions.groupBy { it.getCategory() }.mapValues { entry ->
                            entry.value.sumOf { it.getAmount() }
                        }

                categoryTotals
                        .map { (name, amount) ->
                            val percentage =
                                    if (totalSpent > 0) (amount / totalSpent * 100).toInt() else 0
                            SpendingStat(
                                    name = name,
                                    percentage = percentage,
                                    color =
                                            when (name) {
                                                "Food" -> AccentPink
                                                "Transport" -> Color(0xFF00E5FF)
                                                "Shopping" -> Color(0xFFFFC107)
                                                "Bills" -> Color(0xFF00E676)
                                                else -> Secondary
                                            }
                            )
                        }
                        .sortedByDescending { it.percentage }
            }

    Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            // Header
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                    )
                }
                Text(
                        text = "Analytics",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI Insight Card
            val personality by
                    viewModel.personality.observeAsState(
                            initial =
                                    com.example.expensetrcaker.ai.SpendingAI.calculatePersonality(
                                            emptyList()
                                    )
                    )
            PersonalityCard(personality)

            Spacer(modifier = Modifier.height(32.dp))

            // Glowing Donut Chart (Swirl Style)
            Box(
                    modifier = Modifier.fillMaxWidth().height(280.dp),
                    contentAlignment = Alignment.Center
            ) {
                SwirlChart(stats)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                            "$${"%,.2f".format(totalSpent)}",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                    )
                    Text("Total Spent", color = Color.Gray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Top Spending Categories
            Text(
                    "Top Spending Categories",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(stats) { stat -> CategoryStatRow(stat) }

                if (stats.isEmpty()) {
                    item {
                        Box(
                                modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                                contentAlignment = Alignment.Center
                        ) { Text("No spending data yet", color = Color.Gray) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Active Budgets
            val budgets by viewModel.allBudgets.observeAsState(initial = emptyList())
            var showBudgetDialog by remember { mutableStateOf(false) }

            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                        "Active Budgets",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                )
                TextButton(onClick = { showBudgetDialog = true }) {
                    Text("+ Manage", color = Primary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (budgets.isNotEmpty()) {
                LazyColumn(
                        modifier = Modifier.heightIn(max = 200.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(budgets) { budget ->
                        val spentInCategory =
                                transactions
                                        .filter {
                                            it.getCategory() == budget.getCategory() &&
                                                    it.isExpense()
                                        }
                                        .sumOf { it.getAmount() }
                        BudgetProgressRow(
                                budget.getCategory(),
                                spentInCategory,
                                budget.getLimitAmount()
                        )
                    }
                }
            } else {
                Box(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(SurfaceDark.copy(alpha = 0.3f))
                                        .clickable { showBudgetDialog = true },
                        contentAlignment = Alignment.Center
                ) { Text("No budgets set. Tap to start.", color = Color.Gray, fontSize = 12.sp) }
            }

            if (showBudgetDialog) {
                BudgetDialog(
                        onDismiss = { showBudgetDialog = false },
                        onSave = { category, limit ->
                            viewModel.upsertBudget(category, limit)
                            showBudgetDialog = false
                        }
                )
            }
        }
    }
}

@Composable
fun BudgetDialog(onDismiss: () -> Unit, onSave: (String, Double) -> Unit) {
    var selectedCategory by remember { mutableStateOf("Food") }
    var limitInput by remember { mutableStateOf("") }
    val categories = listOf("Food", "Transport", "Shopping", "Bills", "Rent")

    AlertDialog(
            onDismissRequest = onDismiss,
            containerColor = SurfaceDark,
            title = { Text("Set Category Budget", color = Color.White) },
            text = {
                Column {
                    Text("Select Category", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { cat ->
                            FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = cat },
                                    label = { Text(cat) },
                                    colors =
                                            FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = Primary,
                                                    labelColor = Color.White
                                            )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                            value = limitInput,
                            onValueChange = { limitInput = it },
                            placeholder = { Text("Limit Amount (e.g. 500)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors =
                                    TextFieldDefaults.colors(
                                            focusedContainerColor = BackgroundDark,
                                            unfocusedContainerColor = BackgroundDark,
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White
                                    )
                    )
                }
            },
            confirmButton = {
                TextButton(
                        onClick = {
                            val limit = limitInput.toDoubleOrNull() ?: 0.0
                            if (limit > 0) onSave(selectedCategory, limit)
                        }
                ) { Text("SAVE", color = Primary) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text("CANCEL", color = Color.Gray) }
            }
    )
}

@Composable
fun BudgetProgressRow(category: String, spent: Double, limit: Double) {
    val progress = (spent / limit).coerceIn(0.0, 1.0).toFloat()
    val isOverBudget = spent > limit

    Column(
            modifier =
                    Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceDark.copy(alpha = 0.5f))
                            .padding(16.dp)
    ) {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Text(category, color = Color.White, fontWeight = FontWeight.Bold)
            Text(
                    "$${"%.0f".format(spent)} / $${"%.0f".format(limit)}",
                    color = if (isOverBudget) AccentPink else Color.Gray,
                    fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = if (isOverBudget) AccentPink else AccentGreen,
                trackColor = Color.White.copy(alpha = 0.1f)
        )
    }
}

@Composable
fun SwirlChart(stats: List<SpendingStat>) {
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(stats) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(1f, tween(1500))
    }

    Canvas(modifier = Modifier.size(220.dp)) {
        var currentAngle = -90f

        if (stats.isEmpty()) {
            drawArc(
                    color = SurfaceDark,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 24f, cap = StrokeCap.Round)
            )
            return@Canvas
        }

        stats.forEachIndexed { index, stat ->
            val sweepAngle = (stat.percentage.toFloat() / 100f) * 360f * animationProgress.value

            drawArc(
                    brush = Brush.sweepGradient(listOf(stat.color, stat.color.copy(alpha = 0.3f))),
                    startAngle = currentAngle,
                    sweepAngle = sweepAngle - (if (sweepAngle > 5f) 5f else 0f), // Small gap
                    useCenter = false,
                    style = Stroke(width = 24f, cap = StrokeCap.Round)
            )
            currentAngle += (stat.percentage.toFloat() / 100f) * 360f
        }
    }
}

@Composable
fun CategoryStatRow(stat: SpendingStat) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(stat.name, color = Color.White, fontWeight = FontWeight.Medium)
            Text("${stat.percentage}%", color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
                modifier =
                        Modifier.fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(SurfaceDark)
        ) {
            Box(
                    modifier =
                            Modifier.fillMaxWidth(stat.percentage / 100f)
                                    .fillMaxHeight()
                                    .background(stat.color)
            )
        }
    }
}

data class SpendingStat(val name: String, val percentage: Int, val color: Color)
