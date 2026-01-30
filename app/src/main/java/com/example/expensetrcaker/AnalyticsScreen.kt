package com.example.expensetrcaker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetrcaker.ai.SpendingPersonality
import com.example.expensetrcaker.ui.theme.BackgroundDark
import com.example.expensetrcaker.ui.theme.Primary
import com.example.expensetrcaker.viewmodel.TransactionViewModel

// --- Color Palette based on the image ---
val NeonPink = Color(0xFFFF74B1)
val NeonPurple = Color(0xFFD264FF)
val NeonCyan = Color(0xFF4DE3FF)
val NeonYellow = Color(0xFFFFDD55)
val NeonGreen = Color(0xFF5AFF96)

val GradientPurpleToPink = Brush.horizontalGradient(
    colors = listOf(NeonPurple, NeonPink)
)

data class CategoryAnalyticsData(
    val name: String,
    val spent: Double,
    val limit: Double,
    val percentage: Float,
    val color: Color,
    val icon: ImageVector,
    val status: String? = null,
    val statusColor: Color? = null
)

@Composable
fun AnalyticsScreen(onBack: () -> Unit, viewModel: TransactionViewModel) {
    val transactions by viewModel.allTransactions.observeAsState(initial = emptyList())
    val budgets by viewModel.allBudgets.observeAsState(initial = emptyList())
    val categoryEntities by viewModel.allCategories.observeAsState(initial = emptyList())
    val totalSpent by viewModel.totalExpense.observeAsState(initial = 0.0)
    val personality by viewModel.personality.observeAsState()

    // Process categories for the chart and list
    val analyticsData = remember(transactions, budgets, categoryEntities, totalSpent) {
        val expenseTransactions = transactions.filter { it.isExpense }
        val categoryTotals = expenseTransactions.groupBy { it.category }.mapValues { it.value.sumOf { t -> t.amount } }

        categoryTotals.map { (name, amount) ->
            val budget = budgets.find { it.category == name }
            val limit = budget?.limitAmount ?: (amount * 1.2) // Default limit if none set
            val percentage = if (totalSpent > 0) (amount / totalSpent).toFloat() else 0f
            val catEntity = categoryEntities.find { it.name == name }
            
            CategoryAnalyticsData(
                name = name,
                spent = amount,
                limit = limit,
                percentage = percentage,
                color = catEntity?.let { Color(it.color) } ?: NeonPurple,
                icon = when(name) {
                    "Food" -> Icons.Rounded.Restaurant
                    "Transport" -> Icons.Rounded.DirectionsCar
                    "Shopping" -> Icons.Rounded.ShoppingCart
                    "Bills" -> Icons.Rounded.Receipt
                    else -> Icons.Rounded.Category
                },
                status = if (amount > limit) "Over Budget" else if (amount > limit * 0.8) "Approaching Limit" else "On Track",
                statusColor = if (amount > limit) NeonPink else if (amount > limit * 0.8) NeonYellow else NeonGreen
            )
        }.sortedByDescending { it.spent }
    }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = "Analytics",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Spending Personality Card
            personality?.let {
                SpendingPersonalityCard(it)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Donut Chart Section
            if (analyticsData.isNotEmpty()) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    DonutChart(analyticsData)
                    
                    // Center Text
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Total Spent:", color = Color.Gray, fontSize = 14.sp)
                        Text("$${"%,.2f".format(totalSpent)}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Legend
                ChartLegend(analyticsData)
            } else {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    Text("No spending data to display", color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Budget Management Header
            Text(
                text = "Budget Management",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // List of Categories
            analyticsData.forEach { category ->
                BudgetCard(category)
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun SpendingPersonalityCard(personality: SpendingPersonality) {
    val borderGradient = Brush.linearGradient(
        colors = listOf(NeonPink, NeonPurple, NeonCyan)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, borderGradient, RoundedCornerShape(20.dp))
            .background(Color(0xFF1A1A1A))
    ) {
        Box(modifier = Modifier.matchParentSize().background(
            Brush.verticalGradient(
                colors = listOf(personality.color.copy(alpha = 0.15f), Color.Transparent)
            )
        ))

        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = personality.icon,
                    contentDescription = null,
                    tint = personality.color,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Your Spending Personality:",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = personality.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = personality.insight,
                color = Color.Gray,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun DonutChart(data: List<CategoryAnalyticsData>) {
    val totalAmount = data.sumOf { it.spent }
    
    Canvas(modifier = Modifier.size(220.dp)) {
        val strokeWidth = 35.dp.toPx()
        var startAngle = -90f

        data.forEach { category ->
            val sweepAngle = (category.spent / totalAmount).toFloat() * 360f
            
            drawArc(
                color = category.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth),
                topLeft = Offset(strokeWidth/2, strokeWidth/2),
                size = Size(size.width - strokeWidth, size.height - strokeWidth)
            )
            startAngle += sweepAngle
        }
    }
}

@Composable
fun ChartLegend(data: List<CategoryAnalyticsData>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val rows = data.chunked(3)
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowItems.forEach { item ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(item.color)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(text = item.name, color = Color.LightGray, fontSize = 11.sp)
                            Text(text = "${(item.percentage * 100).toInt()}%", color = Color.Gray, fontSize = 10.sp)
                        }
                    }
                }
                // Fill empty space if row has less than 3 items
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.width(60.dp))
                }
            }
        }
    }
}

@Composable
fun BudgetCard(category: CategoryAnalyticsData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E1E2E))
            .border(0.5.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
    ) {
        Box(modifier = Modifier
            .matchParentSize()
            .background(Brush.horizontalGradient(
                colors = listOf(category.color.copy(alpha = 0.05f), Color.Transparent),
                startX = 0f,
                endX = 300f
            ))
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(category.color.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = category.name,
                            tint = category.color,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = category.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                category.status?.let { status ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(category.statusColor?.copy(alpha = 0.2f) ?: Color.Transparent)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = status,
                            color = category.statusColor ?: Color.Gray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "Spent: $${"%.2f".format(category.spent)}",
                    color = Color.White,
                    fontSize = 12.sp
                )
                Text(
                    text = " / Limit: $${"%.0f".format(category.limit)}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val progress = (category.spent / category.limit).toFloat().coerceIn(0f, 1f)
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (progress > 0.9f) Brush.linearGradient(listOf(NeonPink, NeonPink)) else GradientPurpleToPink)
                )
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    text = "${(progress * 100).toInt()}% used",
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
        }
    }
}
