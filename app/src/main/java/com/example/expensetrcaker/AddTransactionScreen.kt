package com.example.expensetrcaker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Backspace
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetrcaker.ui.theme.AccentGreen
import com.example.expensetrcaker.ui.theme.AccentPink
import com.example.expensetrcaker.ui.theme.BackgroundDark
import com.example.expensetrcaker.ui.theme.GlassSurface
import com.example.expensetrcaker.ui.theme.Primary
import com.example.expensetrcaker.ui.theme.PurpleGradientEnd
import com.example.expensetrcaker.ui.theme.PurpleGradientStart
import com.example.expensetrcaker.ui.theme.SurfaceDark

import com.example.expensetrcaker.viewmodel.TransactionViewModel

@Composable
fun AddTransactionScreen(onBack: () -> Unit, viewModel: TransactionViewModel) {
    var amount by remember { mutableStateOf("0") }
    var selectedCategory by remember { mutableStateOf("Food") }
    var isExpense by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = "Add Transaction",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.size(48.dp)) // Balance header
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Amount Display (Neon Box)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceDark)
                    .border(1.dp, Brush.horizontalGradient(listOf(PurpleGradientStart, PurpleGradientEnd)), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$$amount",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Transaction Type Toggle (Income / Expense)
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(SurfaceDark.copy(alpha = 0.5f))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp)),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (isExpense) AccentPink.copy(alpha = 0.2f) else Color.Transparent)
                        .clickable { isExpense = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Expense", color = if (isExpense) AccentPink else Color.Gray, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (!isExpense) AccentGreen.copy(alpha = 0.2f) else Color.Transparent)
                        .clickable { isExpense = false },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Income", color = if (!isExpense) AccentGreen else Color.Gray, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Wallet Selection
            val wallets by viewModel.allWallets.collectAsState(initial = emptyList())
            var selectedWalletId by remember { mutableStateOf(1L) }

            Text("Select Wallet", color = Color.Gray, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(wallets) { wallet ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selectedWalletId == wallet.id) Primary.copy(alpha = 0.2f) else SurfaceDark)
                            .border(1.dp, if (selectedWalletId == wallet.id) Primary else Color.Transparent, RoundedCornerShape(12.dp))
                            .clickable { selectedWalletId = wallet.id }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(wallet.name, color = if (selectedWalletId == wallet.id) Color.White else Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Categories (Glowing Bubbles)
            Text("Category", color = Color.Gray, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val expenseCategories = listOf(
                    CategoryItem("Food", Icons.Rounded.Fastfood, AccentPink),
                    CategoryItem("Transport", Icons.Rounded.DirectionsCar, Color(0xFF00E5FF)),
                    CategoryItem("Shopping", Icons.Rounded.ShoppingBag, Color(0xFFFFC107)),
                    CategoryItem("Bills", Icons.Rounded.Receipt, Color(0xFF00E676)),
                    CategoryItem("Rent", Icons.Rounded.Home, Color(0xFF9C27B0))
                )
                val incomeCategories = listOf(
                    CategoryItem("Salary", Icons.Rounded.Check, AccentGreen),
                    CategoryItem("Bonus", Icons.Rounded.Check, Color(0xFF00E5FF)),
                    CategoryItem("Gift", Icons.Rounded.Check, Color(0xFFFFC107)),
                    CategoryItem("Others", Icons.Rounded.Check, Color(0xFF03DAC6))
                )
                val categories = if (isExpense) expenseCategories else incomeCategories
                
                items(categories) { item ->
                    CategoryBubble(
                        item = item,
                        isSelected = selectedCategory == item.name,
                        onClick = { selectedCategory = item.name }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Neon Numeric Keypad
            NumericKeypad(
                onNumberClick = { num ->
                    if (amount == "0" && num != ".") amount = num 
                    else if (num == "." && !amount.contains(".")) amount += num
                    else if (num != ".") amount += num
                },
                onDeleteClick = {
                    if (amount.length > 1) amount = amount.dropLast(1) else amount = "0"
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = { 
                    val amtValue = amount.toDoubleOrNull() ?: 0.0
                    if (amtValue > 0) {
                        viewModel.addTransaction(
                            title = selectedCategory,
                            amount = amtValue,
                            category = selectedCategory,
                            isExpense = isExpense,
                            walletId = selectedWalletId
                        )
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(elevation = 16.dp, spotColor = Primary.copy(alpha = 0.5f), ambientColor = Primary, shape = RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(listOf(PurpleGradientStart, PurpleGradientEnd)),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("SAVE TRANSACTION", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CategoryBubble(item: CategoryItem, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(if (isSelected) item.color.copy(alpha = 0.2f) else SurfaceDark)
                .border(
                    if (isSelected) 2.dp else 0.dp,
                    if (isSelected) item.color else Color.Transparent,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(item.icon, contentDescription = item.name, tint = if (isSelected) item.color else Color.Gray)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(item.name, color = if (isSelected) Color.White else Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun NumericKeypad(onNumberClick: (String) -> Unit, onDeleteClick: () -> Unit) {
    val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "DEL")
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(keys) { key ->
            KeypadButton(key, onNumberClick, onDeleteClick)
        }
    }
}

@Composable
fun KeypadButton(key: String, onNumberClick: (String) -> Unit, onDeleteClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1.5f)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                if (key == "DEL") onDeleteClick() else onNumberClick(key)
            }
            .background(SurfaceDark.copy(alpha = 0.5f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (key == "DEL") {
            Icon(Icons.Rounded.Backspace, contentDescription = "Delete", tint = Color.White)
        } else {
            Text(key, fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}

data class CategoryItem(val name: String, val icon: ImageVector, val color: Color)
