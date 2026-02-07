package com.example.expensetrcaker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetrcaker.ui.theme.*
import com.example.expensetrcaker.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetsScreen(onBack: () -> Unit, viewModel: TransactionViewModel) {
    val totalExpense by viewModel.totalExpense.collectAsState(initial = 0.0)

    Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            // Top App Bar
            Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 48.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                        text = "Budgets",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                )
                IconButton(onClick = { /* Refresh */}) {
                    Icon(Icons.Rounded.Refresh, contentDescription = "Refresh", tint = Color.White)
                }
            }

            // Period Selector
            Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PeriodChip("This Month", true)
                PeriodChip("Last Month", false)
                PeriodChip("May 2024", false)
            }

            // Total Monthly Summary Card
            Box(
                    modifier =
                            Modifier.fillMaxWidth()
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(
                                            Brush.horizontalGradient(
                                                    colors =
                                                            listOf(
                                                                    PurpleGradientStart.copy(
                                                                            alpha = 0.3f
                                                                    ),
                                                                    BlueGradientEnd.copy(
                                                                            alpha = 0.3f
                                                                    )
                                                            )
                                            )
                                    )
                                    .padding(20.dp)
            ) {
                Column {
                    Text("Total Monthly", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                    Icons.Rounded.AttachMoney,
                                    contentDescription = null,
                                    tint = AccentGreen,
                                    modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                    "1455",
                                    color = Color.White,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                                "$${"%.2f".format(totalExpense)}",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Dark Aport", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                            progress = { 0.31f },
                            modifier =
                                    Modifier.fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                            color = AccentPink,
                            trackColor = Color.White.copy(alpha = 0.2f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Budget Categories List
            LazyColumn(
                    contentPadding = PaddingValues(bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    BudgetCategoryItem(
                            icon = Icons.Rounded.Restaurant,
                            name = "Food",
                            amount = 400.0,
                            color = Color(0xFF8B5CF6)
                    )
                }
                item {
                    BudgetCategoryItem(
                            icon = Icons.Rounded.ShoppingBag,
                            name = "Shopping",
                            amount = 330.0,
                            color = Color(0xFF6366F1)
                    )
                }
                item {
                    BudgetCategoryItem(
                            icon = Icons.Rounded.DirectionsCar,
                            name = "Transport",
                            amount = 300.0,
                            color = Color(0xFF8B5CF6)
                    )
                }
                item {
                    BudgetCategoryItem(
                            icon = Icons.Rounded.Celebration,
                            name = "Entertainment",
                            amount = 150.0,
                            color = Color(0xFF10B981)
                    )
                }
            }
        }
    }
}

@Composable
fun PeriodChip(text: String, isSelected: Boolean) {
    Box(
            modifier =
                    Modifier.clip(RoundedCornerShape(16.dp))
                            .background(
                                    if (isSelected) PurpleGradientStart.copy(alpha = 0.4f)
                                    else Color.White.copy(alpha = 0.1f)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
                text,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun BudgetCategoryItem(icon: ImageVector, name: String, amount: Double, color: Color) {
    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(SurfaceDark.copy(alpha = 0.4f))
                            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                    modifier =
                            Modifier.size(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
        Text(
                "$${"%.0f".format(amount)}",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
        )
    }
}
