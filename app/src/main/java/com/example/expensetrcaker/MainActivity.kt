package com.example.expensetrcaker

import com.example.expensetrcaker.viewmodel.TransactionViewModel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetrcaker.ui.theme.AccentGreen
import com.example.expensetrcaker.ui.theme.AccentPink
import com.example.expensetrcaker.ui.theme.BackgroundDark
import com.example.expensetrcaker.ui.theme.BlueGradientEnd
import com.example.expensetrcaker.ui.theme.BlueGradientStart
import com.example.expensetrcaker.ui.theme.ExpenseTrackerTheme
import com.example.expensetrcaker.ui.theme.GlassBorder
import com.example.expensetrcaker.ui.theme.GlassSurface
import com.example.expensetrcaker.ui.theme.Primary
import com.example.expensetrcaker.ui.theme.PurpleGradientEnd
import com.example.expensetrcaker.ui.theme.PurpleGradientStart
import com.example.expensetrcaker.ui.theme.SurfaceDark

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme(darkTheme = true) {
                val viewModel: TransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                var currentScreen by remember { mutableStateOf("home") }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            if (currentScreen == "home" || currentScreen == "analytics" || currentScreen == "wallets" || currentScreen == "profile") {
                                BottomNavBar( // Renamed from CustomBottomNavigation to match existing function
                                    currentScreen = currentScreen,
                                    onScreenSelected = { currentScreen = it }
                                )
                            }
                        },
                        floatingActionButton = {
                            if (currentScreen == "home") {
                                FloatingActionButton(
                                    onClick = { currentScreen = "add_transaction" },
                                    containerColor = Primary,
                                    contentColor = Color.White,
                                    shape = CircleShape,
                                    modifier = Modifier.offset(y = 40.dp)
                                ) {
                                    Icon(Icons.Filled.Add, contentDescription = "Add")
                                }
                            }
                        },
                        floatingActionButtonPosition = FabPosition.Center
                    ) { innerPadding ->
                        when (currentScreen) {
                            "home" -> HomeScreen(Modifier.padding(innerPadding), viewModel)
                            "analytics" -> AnalyticsScreen(
                                onBack = { currentScreen = "home" },
                                viewModel = viewModel
                            )
                            "wallets" -> WalletsScreen(
                                onBack = { currentScreen = "home" },
                                viewModel = viewModel
                            )
                            "profile" -> ProfileScreen(
                                onBack = { currentScreen = "home" }
                            )
                            "add_transaction" -> AddTransactionScreen(
                                onBack = { currentScreen = "home" },
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(currentScreen: String, onScreenSelected: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp, start = 24.dp, end = 24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            shape = RoundedCornerShape(35.dp),
            color = Color(0xFF1E1E1E).copy(alpha = 0.8f),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavIcon(
                    icon = Icons.Rounded.Home,
                    isSelected = currentScreen == "home",
                    onClick = { onScreenSelected("home") }
                )
                NavIcon(
                    icon = Icons.Rounded.BarChart,
                    isSelected = currentScreen == "analytics",
                    onClick = { onScreenSelected("analytics") }
                )
                Spacer(modifier = Modifier.width(60.dp)) // Space for FAB
                NavIcon(
                    icon = Icons.Rounded.AccountBalanceWallet,
                    isSelected = currentScreen == "wallets",
                    onClick = { onScreenSelected("wallets") }
                )
                NavIcon(
                    icon = Icons.Rounded.Person,
                    isSelected = currentScreen == "profile",
                    onClick = { onScreenSelected("profile") }
                )
            }
        }
    }
}

@Composable
fun NavIcon(icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (isSelected) Primary else Color.Gray,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: TransactionViewModel) {
    val transactions by viewModel.allTransactions.collectAsState(initial = emptyList())
    val totalIncome by viewModel.totalIncome.collectAsState(initial = 0.0)
    val totalExpense by viewModel.totalExpense.collectAsState(initial = 0.0)
    val balance by viewModel.totalBalance.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // Ambient Light Orbs (Background Glow)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(PurpleGradientStart.copy(alpha = 0.4f), Color.Transparent),
                    center = Offset(0f, 0f),
                    radius = canvasWidth * 0.8f
                ),
                center = Offset(0f, 0f),
                radius = canvasWidth * 0.8f
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(BlueGradientEnd.copy(alpha = 0.2f), Color.Transparent),
                    center = Offset(canvasWidth, canvasHeight * 0.6f),
                    radius = canvasWidth * 0.7f
                ),
                center = Offset(canvasWidth, canvasHeight * 0.6f),
                radius = canvasWidth * 0.7f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Good Evening,",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "Niloy",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                // Profile Avatar with Ring
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(SurfaceDark),
                    contentAlignment = Alignment.Center
                ) {
                    Text("N", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Premium Balance Card
            GlassyCard(modifier = Modifier.height(220.dp)) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Total Balance",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$${"%,.2f".format(balance)}",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 40.sp,
                        letterSpacing = (-1).sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FinanceIndicator(
                            label = "Income",
                            amount = "+$${"%,.0f".format(totalIncome)}",
                            color = AccentGreen,
                            icon = Icons.Default.Add
                        )
                        Box(modifier = Modifier.width(1.dp).height(30.dp).background(Color.White.copy(alpha = 0.2f)))
                        FinanceIndicator(
                            label = "Expense",
                            amount = "-$${"%,.0f".format(totalExpense)}",
                            color = AccentPink,
                            icon = Icons.Default.Add
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Recent Transactions
            Text(
                text = "Recent Transactions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (transactions.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                            Text("No transactions found", color = Color.Gray)
                        }
                    }
                } else {
                    itemsIndexed(transactions) { index, item ->
                        TransactionItem(
                            Transaction(
                                title = item.title,
                                date = java.text.SimpleDateFormat("MMM dd, hh:mm a", java.util.Locale.getDefault()).format(java.util.Date(item.date)),
                                amount = if (item.isExpense) "-$${item.amount}" else "+$${item.amount}",
                                icon = when (item.category) {
                                    "Food" -> Icons.Rounded.Restaurant
                                    "Transport" -> Icons.Rounded.DirectionsCar
                                    "Shopping" -> Icons.Rounded.ShoppingCart
                                    "Entertainment" -> Icons.Rounded.Coffee
                                    "Health" -> Icons.Rounded.FitnessCenter
                                    else -> Icons.Rounded.Receipt
                                },
                                color = if (item.isExpense) AccentPink else Primary
                            ),
                            delayMillis = index * 100,
                            onDelete = { viewModel.deleteTransaction(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FinanceIndicator(label: String, amount: String, color: Color, icon: ImageVector) {
    Column {
        Text(text = label, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = amount, color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun GlassyCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF2D2D2D).copy(alpha = 0.6f),
                        Color(0xFF1E1E1E).copy(alpha = 0.3f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            Color.White.copy(alpha = 0.05f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                ),
                RoundedCornerShape(32.dp)
            )
    ) {
        content()
    }
}

@Composable
fun TransactionItem(transaction: Transaction, delayMillis: Int, onDelete: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delayMillis.toLong())
        isVisible = true
    }
    
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500)
    )
    val animatedSlide by animateFloatAsState(
        targetValue = if (isVisible) 0f else 50f,
        animationSpec = tween(durationMillis = 500)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = animatedAlpha
                translationY = animatedSlide
            }
            .clip(RoundedCornerShape(24.dp))
            .background(SurfaceDark.copy(alpha = 0.6f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(transaction.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = transaction.icon,
                contentDescription = null,
                tint = transaction.color,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = transaction.date,
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = transaction.amount,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            IconButton(onClick = onDelete, modifier = Modifier.size(24.dp).padding(top = 4.dp)) {
                Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = AccentPink, modifier = Modifier.size(16.dp))
            }
        }
    }
}

data class Transaction(
    val title: String,
    val date: String,
    val amount: String,
    val icon: ImageVector,
    val color: Color
)