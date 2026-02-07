package com.example.expensetrcaker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetrcaker.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(onBack: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Expense, 1: Income
    val categories =
            listOf(
                    CategoryUiModel("Food", Icons.Rounded.Restaurant, Color(0xFF4CAF50), "$400"),
                    CategoryUiModel(
                            "Shopping",
                            Icons.Rounded.ShoppingCart,
                            Color(0xFF9C27B0),
                            "$330"
                    ),
                    CategoryUiModel(
                            "Transport",
                            Icons.Rounded.DirectionsCar,
                            Color(0xFF2196F3),
                            "$300"
                    ),
                    CategoryUiModel(
                            "Entertainment",
                            Icons.Rounded.ConfirmationNumber,
                            Color(0xFFFF9800),
                            "$150"
                    )
            )

    val manageCategories =
            listOf(
                    CategoryManageItem("Food", Icons.Rounded.Restaurant, Color(0xFF4CAF50)),
                    CategoryManageItem("Shopping", Icons.Rounded.ShoppingCart, Color(0xFF9C27B0)),
                    CategoryManageItem("Transport", Icons.Rounded.DirectionsCar, Color(0xFF2196F3)),
                    CategoryManageItem("Health", Icons.Rounded.FitnessCenter, Color(0xFFE91E63)),
                    CategoryManageItem(
                            "Groceries",
                            Icons.Rounded.LocalGroceryStore,
                            Color(0xFFFFC107)
                    )
            )

    Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            // Top Bar
            Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 48.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                        text = "Categories",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                )
                IconButton(onClick = { /* Sync/Refresh */}) {
                    Icon(Icons.Rounded.Sync, contentDescription = "Sync", tint = Color.White)
                }
            }

            // Search Bar
            Box(
                    modifier =
                            Modifier.fillMaxWidth()
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(25.dp))
                                    .background(SurfaceDark.copy(alpha = 0.5f))
                                    .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Search, contentDescription = "Search", tint = Color.Gray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Search", color = Color.Gray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tabs: Expense / Income
            Row(
                    modifier =
                            Modifier.fillMaxWidth()
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFF2D2D2D))
                                    .padding(4.dp)
            ) {
                Box(
                        modifier =
                                Modifier.weight(1f)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                                if (selectedTab == 0) Primary else Color.Transparent
                                        )
                                        .clickable { selectedTab = 0 },
                        contentAlignment = Alignment.Center
                ) {
                    Text(
                            "Expense",
                            color = if (selectedTab == 0) Color.White else Color.Gray,
                            fontSize = 14.sp
                    )
                }
                Box(
                        modifier =
                                Modifier.weight(1f)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                                if (selectedTab == 1) Primary else Color.Transparent
                                        )
                                        .clickable { selectedTab = 1 },
                        contentAlignment = Alignment.Center
                ) {
                    Text(
                            "Income",
                            color = if (selectedTab == 1) Color.White else Color.Gray,
                            fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Grid Categories
            LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.height(200.dp) // Fixed height for grid part or flexible
            ) { items(categories) { category -> CategoryGridItem(category) } }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Manage", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Manage List
            LazyColumn(
                    contentPadding = PaddingValues(bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(manageCategories.size) { index -> CategoryListItem(manageCategories[index]) }
            }
        }
    }
}

@Composable
fun CategoryGridItem(category: CategoryUiModel) {
    Box(
            modifier =
                    Modifier.aspectRatio(1.4f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(SurfaceDark.copy(alpha = 0.6f))
                            .padding(16.dp)
    ) {
        Column {
            Box(
                    modifier =
                            Modifier.size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(category.color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
            ) {
                Icon(
                        category.icon,
                        contentDescription = null,
                        tint = category.color,
                        modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                    category.name,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
            )
            Text(category.amount, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun CategoryListItem(item: CategoryManageItem) {
    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceDark.copy(alpha = 0.4f))
                            .clickable { /* Manage */}
                            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                    modifier =
                            Modifier.size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(item.color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
            ) {
                Icon(
                        item.icon,
                        contentDescription = null,
                        tint = item.color,
                        modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(item.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
        Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = Color.Gray)
    }
}

data class CategoryUiModel(
        val name: String,
        val icon: ImageVector,
        val color: Color,
        val amount: String
)

data class CategoryManageItem(val name: String, val icon: ImageVector, val color: Color)
