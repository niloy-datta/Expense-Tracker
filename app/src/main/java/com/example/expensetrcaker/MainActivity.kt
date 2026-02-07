package com.example.expensetrcaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetrcaker.ui.theme.BackgroundDark
import com.example.expensetrcaker.ui.theme.ExpenseTrackerTheme
import com.example.expensetrcaker.ui.theme.Primary
import com.example.expensetrcaker.ui.theme.PurpleGradientStart
import com.example.expensetrcaker.viewmodel.TransactionViewModel

class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                enableEdgeToEdge()
                setContent {
                        ExpenseTrackerTheme(darkTheme = true) {
                                val viewModel: TransactionViewModel =
                                        androidx.lifecycle.viewmodel.compose.viewModel()
                                var currentScreen by remember { mutableStateOf("home") }
                                var transactionToEdit by remember {
                                        mutableStateOf<
                                                com.example.expensetrcaker.data.TransactionEntity?>(
                                                null
                                        )
                                }

                                Surface(
                                        modifier = Modifier.fillMaxSize(),
                                        color = MaterialTheme.colorScheme.background
                                ) {
                                        Scaffold(
                                                bottomBar = {
                                                        if (currentScreen == "home" ||
                                                                        currentScreen ==
                                                                                "analytics" ||
                                                                        currentScreen ==
                                                                                "wallets" ||
                                                                        currentScreen == "settings"
                                                        ) {
                                                                BottomNavBar(
                                                                        currentScreen =
                                                                                currentScreen,
                                                                        onScreenSelected = {
                                                                                currentScreen = it
                                                                        }
                                                                )
                                                        }
                                                },
                                                floatingActionButton = {
                                                        if (currentScreen == "home") {
                                                                Box(
                                                                        modifier =
                                                                                Modifier.offset(
                                                                                                y =
                                                                                                        45.dp
                                                                                        )
                                                                                        .size(65.dp)
                                                                                        .shadow(
                                                                                                15.dp,
                                                                                                CircleShape,
                                                                                                spotColor =
                                                                                                        Primary
                                                                                        )
                                                                                        .clip(
                                                                                                CircleShape
                                                                                        )
                                                                                        .background(
                                                                                                Brush.linearGradient(
                                                                                                        listOf(
                                                                                                                PurpleGradientStart,
                                                                                                                Color(
                                                                                                                        0xFF4A00E0
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                        .noRippleClickable {
                                                                                                transactionToEdit =
                                                                                                        null
                                                                                                currentScreen =
                                                                                                        "add_transaction"
                                                                                        },
                                                                        contentAlignment =
                                                                                Alignment.Center
                                                                ) {
                                                                        Icon(
                                                                                Icons.Filled.Add,
                                                                                contentDescription =
                                                                                        "Add",
                                                                                tint = Color.White,
                                                                                modifier =
                                                                                        Modifier.size(
                                                                                                32.dp
                                                                                        )
                                                                        )
                                                                }
                                                        }
                                                },
                                                floatingActionButtonPosition = FabPosition.Center
                                        ) { innerPadding ->
                                                when (currentScreen) {
                                                        "home" ->
                                                                HomeScreen(
                                                                        Modifier.padding(
                                                                                innerPadding
                                                                        ),
                                                                        viewModel,
                                                                        onTransactionClick = { id ->
                                                                                currentScreen =
                                                                                        "transaction_detail/$id"
                                                                        }
                                                                )
                                                        "analytics" ->
                                                                AnalyticsScreen(
                                                                        onBack = {
                                                                                currentScreen =
                                                                                        "home"
                                                                        },
                                                                        viewModel = viewModel
                                                                )
                                                        "wallets" ->
                                                                WalletsScreen(
                                                                        onBack = {
                                                                                currentScreen =
                                                                                        "home"
                                                                        },
                                                                        viewModel = viewModel
                                                                )
                                                        "profile" ->
                                                                ProfileScreen(
                                                                        onBack = {
                                                                                currentScreen =
                                                                                        "home"
                                                                        },
                                                                        viewModel = viewModel
                                                                )
                                                        "settings" ->
                                                                SettingsScreen(
                                                                        onBack = {
                                                                                currentScreen =
                                                                                        "home"
                                                                        },
                                                                        onNavigate = {
                                                                                currentScreen = it
                                                                        }
                                                                )
                                                        "categories" ->
                                                                CategoriesScreen(
                                                                        onBack = {
                                                                                currentScreen =
                                                                                        "settings"
                                                                        }
                                                                )
                                                        "add_transaction" ->
                                                                AddTransactionScreen(
                                                                        onBack = {
                                                                                currentScreen =
                                                                                        "home"
                                                                        },
                                                                        viewModel = viewModel,
                                                                        existingTransaction =
                                                                                transactionToEdit
                                                                )
                                                        else -> {
                                                                if (currentScreen.startsWith(
                                                                                "transaction_detail/"
                                                                        )
                                                                ) {
                                                                        val transactionId =
                                                                                currentScreen
                                                                                        .substringAfter(
                                                                                                "transaction_detail/"
                                                                                        )
                                                                                        .toIntOrNull()
                                                                                        ?: 0
                                                                        TransactionDetailScreen(
                                                                                transactionId =
                                                                                        transactionId,
                                                                                onBack = {
                                                                                        currentScreen =
                                                                                                "home"
                                                                                },
                                                                                onEdit = {
                                                                                        transaction
                                                                                        ->
                                                                                        transactionToEdit =
                                                                                                transaction
                                                                                        currentScreen =
                                                                                                "add_transaction"
                                                                                },
                                                                                viewModel =
                                                                                        viewModel
                                                                        )
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                }
        }
}

// Utility for clickable without ripple effect
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
        this.then(
                Modifier.clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = onClick
                )
        )

@Composable
fun BottomNavBar(currentScreen: String, onScreenSelected: (String) -> Unit) {
        Box(
                modifier =
                        Modifier.fillMaxWidth().padding(bottom = 20.dp, start = 16.dp, end = 16.dp),
                contentAlignment = Alignment.BottomCenter
        ) {
                Surface(
                        modifier = Modifier.fillMaxWidth().height(75.dp),
                        shape = RoundedCornerShape(25.dp),
                        color = Color(0xFF151515).copy(alpha = 0.9f),
                        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))
                ) {
                        Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                NavIcon(
                                        icon = Icons.Rounded.Home,
                                        label = "Home",
                                        isSelected = currentScreen == "home",
                                        onClick = { onScreenSelected("home") }
                                )
                                NavIcon(
                                        icon = Icons.Rounded.BarChart,
                                        label = "Analytics",
                                        isSelected = currentScreen == "analytics",
                                        onClick = { onScreenSelected("analytics") }
                                )
                                Spacer(modifier = Modifier.width(50.dp)) // Space for FAB
                                NavIcon(
                                        icon = Icons.Rounded.AccountBalanceWallet,
                                        label = "Wallets",
                                        isSelected = currentScreen == "wallets",
                                        onClick = { onScreenSelected("wallets") }
                                )
                                NavIcon(
                                        icon = Icons.Rounded.Settings,
                                        label = "Settings",
                                        isSelected = currentScreen == "settings",
                                        onClick = { onScreenSelected("settings") }
                                )
                        }
                }
        }
}

@Composable
fun NavIcon(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.noRippleClickable(onClick = onClick)
        ) {
                Icon(
                        icon,
                        contentDescription = null,
                        tint = if (isSelected) Primary else Color.Gray.copy(alpha = 0.6f),
                        modifier = Modifier.size(26.dp)
                )
                Text(
                        text = label,
                        fontSize = 10.sp,
                        color = if (isSelected) Primary else Color.Gray.copy(alpha = 0.6f),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
        }
}

@Composable
fun HomeScreen(
        modifier: Modifier = Modifier,
        viewModel: TransactionViewModel,
        onTransactionClick: (Int) -> Unit
) {
        val transactions by viewModel.allTransactions.observeAsState(initial = emptyList())
        val totalIncome by viewModel.totalIncome.observeAsState(initial = 0.0)
        val totalExpense by viewModel.totalExpense.observeAsState(initial = 0.0)
        val balance by viewModel.totalBalance.observeAsState(initial = 0.0)

        Box(modifier = modifier.fillMaxSize().background(BackgroundDark)) {
                // Gradient Background Glows
                Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                                brush =
                                        Brush.radialGradient(
                                                colors =
                                                        listOf(
                                                                Color(0xFF5D26C1)
                                                                        .copy(alpha = 0.3f),
                                                                Color.Transparent
                                                        ),
                                                center = Offset(0f, size.height * 0.2f),
                                                radius = size.width * 0.9f
                                        ),
                                center = Offset(0f, size.height * 0.2f),
                                radius = size.width * 0.9f
                        )
                        drawCircle(
                                brush =
                                        Brush.radialGradient(
                                                colors =
                                                        listOf(
                                                                Color(0xFF2196F3)
                                                                        .copy(alpha = 0.2f),
                                                                Color.Transparent
                                                        ),
                                                center = Offset(size.width, size.height * 0.5f),
                                                radius = size.width * 0.8f
                                        ),
                                center = Offset(size.width, size.height * 0.5f),
                                radius = size.width * 0.8f
                        )
                }

                Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                        Spacer(modifier = Modifier.height(20.dp))

                        // Glassy Balance Card
                        Box(
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .height(220.dp)
                                                .clip(RoundedCornerShape(30.dp))
                                                .background(
                                                        Brush.linearGradient(
                                                                listOf(
                                                                        Color.White.copy(
                                                                                alpha = 0.1f
                                                                        ),
                                                                        Color.White.copy(
                                                                                alpha = 0.02f
                                                                        )
                                                                )
                                                        )
                                                )
                                                .border(
                                                        0.5.dp,
                                                        Color.White.copy(alpha = 0.15f),
                                                        RoundedCornerShape(30.dp)
                                                )
                        ) {
                                Column(
                                        modifier = Modifier.fillMaxSize().padding(24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                        Text(
                                                text = "Total Balance",
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Medium
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                                text = "$${"%,.2f".format(balance)}",
                                                color = Color.White,
                                                fontSize = 44.sp,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = (-1).sp
                                        )
                                        Spacer(modifier = Modifier.weight(1f))

                                        Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceAround,
                                                verticalAlignment = Alignment.CenterVertically
                                        ) {
                                                FinanceIndicatorClone(
                                                        label = "Income",
                                                        amount = "$${"%,.0f".format(totalIncome)}",
                                                        color = Color(0xFF00E676),
                                                        isIncome = true
                                                )
                                                Box(
                                                        modifier =
                                                                Modifier.width(1.dp)
                                                                        .height(40.dp)
                                                                        .background(
                                                                                Color.White.copy(
                                                                                        alpha = 0.1f
                                                                                )
                                                                        )
                                                )
                                                FinanceIndicatorClone(
                                                        label = "Expense",
                                                        amount = "$${"%,.0f".format(totalExpense)}",
                                                        color = Color(0xFFFF5252),
                                                        isIncome = false
                                                )
                                        }
                                }
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                                text = "Recent Transactions",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(
                                contentPadding = PaddingValues(bottom = 100.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                modifier = Modifier.fillMaxSize()
                        ) {
                                itemsIndexed(transactions) { index, item ->
                                        TransactionItemClone(
                                                item = item,
                                                onDelete = { viewModel.deleteTransaction(item) },
                                                onClick = { onTransactionClick(item.id) }
                                        )
                                }
                        }
                }
        }
}

@Composable
fun FinanceIndicatorClone(label: String, amount: String, color: Color, isIncome: Boolean) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                                if (isIncome) Icons.Rounded.KeyboardArrowUp
                                else Icons.Rounded.KeyboardArrowDown,
                                contentDescription = null,
                                tint = color.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = label, color = Color.Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                        text = (if (isIncome) "+" else "-") + amount,
                        color = color,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                )
        }
}

@Composable
fun TransactionItemClone(
        item: com.example.expensetrcaker.data.TransactionEntity,
        onDelete: () -> Unit,
        onClick: () -> Unit
) {
        Box(
                modifier =
                        Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF1A1A1A))
                                .border(
                                        0.5.dp,
                                        Color.White.copy(alpha = 0.05f),
                                        RoundedCornerShape(20.dp)
                                )
                                .padding(16.dp)
                                .noRippleClickable(onClick)
        ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                        // Icon
                        val iconColor = if (item.isExpense) Color(0xFF9C27B0) else Color(0xFF2196F3)
                        Box(
                                modifier =
                                        Modifier.size(48.dp)
                                                .clip(CircleShape)
                                                .background(
                                                        Brush.linearGradient(
                                                                listOf(
                                                                        iconColor.copy(
                                                                                alpha = 0.4f
                                                                        ),
                                                                        iconColor.copy(alpha = 0.1f)
                                                                )
                                                        )
                                                ),
                                contentAlignment = Alignment.Center
                        ) {
                                Icon(
                                        when (item.category) {
                                                "Food" -> Icons.Rounded.Restaurant
                                                "Transport" -> Icons.Rounded.DirectionsCar
                                                "Shopping" -> Icons.Rounded.ShoppingCart
                                                else -> Icons.Rounded.Receipt
                                        },
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                                Text(
                                        text = item.title,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp
                                )
                                Text(
                                        text =
                                                java.text.SimpleDateFormat(
                                                                "EEEE, h:mm a",
                                                                java.util.Locale.getDefault()
                                                        )
                                                        .format(java.util.Date(item.date)),
                                        color = Color.Gray,
                                        fontSize = 12.sp
                                )
                        }

                        Text(
                                text = (if (item.isExpense) "-" else "+") + "$${item.amount}",
                                color =
                                        if (item.isExpense) Color(0xFFFF5252)
                                        else Color(0xFF00E676),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                        )
                }
        }
}
