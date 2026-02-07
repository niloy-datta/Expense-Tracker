package com.example.expensetrcaker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetrcaker.ui.theme.*
import com.example.expensetrcaker.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionDetailScreen(
        transactionId: Int,
        onBack: () -> Unit,
        onEdit: (com.example.expensetrcaker.data.TransactionEntity) -> Unit,
        viewModel: TransactionViewModel
) {
        val transactions by viewModel.allTransactions.collectAsState(initial = emptyList())
        val transaction = transactions.find { it.id == transactionId }

        if (transaction == null) {
                // Handle loading or not found
                Box(
                        modifier = Modifier.fillMaxSize().background(BackgroundDark),
                        contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = Primary) }
                return
        }

        var note by remember { mutableStateOf("") } // In real app, this would be from DB

        Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
                Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                        // Header
                        Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                IconButton(onClick = onBack) {
                                        Icon(
                                                Icons.Rounded.ArrowBack,
                                                contentDescription = "Back",
                                                tint = Color.White
                                        )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                        Text(
                                                text = transaction.title,
                                                style = MaterialTheme.typography.headlineSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                        )
                                }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Category Chip (Right aligned or floating?)
                        // Screenshot shows it floating or integrated. Let's put it aligned to end
                        // or below
                        // header.
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                        ) {
                                Box(
                                        modifier =
                                                Modifier.clip(RoundedCornerShape(20.dp))
                                                        .background(SurfaceDark)
                                                        .clickable { /* Edit Category */}
                                                        .padding(
                                                                horizontal = 16.dp,
                                                                vertical = 8.dp
                                                        )
                                ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                        when (transaction.category) {
                                                                "Food" -> Icons.Rounded.Fastfood
                                                                "Transport" ->
                                                                        Icons.Rounded.DirectionsCar
                                                                "Shopping" ->
                                                                        Icons.Rounded.ShoppingBag
                                                                else -> Icons.Rounded.Category
                                                        },
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                        transaction.category,
                                                        color = Color.White,
                                                        fontSize = 14.sp
                                                )
                                                Icon(
                                                        Icons.Rounded.ChevronRight,
                                                        contentDescription = null,
                                                        tint = Color.Gray,
                                                        modifier = Modifier.size(16.dp)
                                                )
                                        }
                                }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Main Detail Card
                        Box(
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .clip(RoundedCornerShape(32.dp))
                                                .background(
                                                        Brush.verticalGradient(
                                                                colors =
                                                                        listOf(
                                                                                SurfaceDark.copy(
                                                                                        alpha = 0.8f
                                                                                ),
                                                                                SurfaceDark.copy(
                                                                                        alpha = 0.4f
                                                                                )
                                                                        )
                                                        )
                                                )
                                                .border(
                                                        1.dp,
                                                        Color.White.copy(alpha = 0.05f),
                                                        RoundedCornerShape(32.dp)
                                                )
                                                .padding(32.dp)
                        ) {
                                Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.fillMaxWidth()
                                ) {
                                        // Amount
                                        Text(
                                                text =
                                                        (if (transaction.isExpense) "-" else "+") +
                                                                "$${"%.2f".format(transaction.amount)}",
                                                fontSize = 40.sp,
                                                fontWeight = FontWeight.Bold,
                                                color =
                                                        if (transaction.isExpense) AccentPink
                                                        else AccentGreen
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Date & Time
                                        Text(
                                                text =
                                                        SimpleDateFormat(
                                                                        "MMM dd, yyyy",
                                                                        Locale.getDefault()
                                                                )
                                                                .format(Date(transaction.date)),
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontSize = 16.sp
                                        )
                                        Text(
                                                text =
                                                        SimpleDateFormat(
                                                                        "h:mm a",
                                                                        Locale.getDefault()
                                                                )
                                                                .format(Date(transaction.date)),
                                                color = Color.White.copy(alpha = 0.5f),
                                                fontSize = 14.sp
                                        )

                                        Spacer(modifier = Modifier.height(32.dp))

                                        // Note Input
                                        TextField(
                                                value = note,
                                                onValueChange = { note = it },
                                                placeholder = {
                                                        Text("Enter note...", color = Color.Gray)
                                                },
                                                modifier =
                                                        Modifier.fillMaxWidth()
                                                                .clip(RoundedCornerShape(16.dp))
                                                                .background(
                                                                        Color.Black.copy(
                                                                                alpha = 0.2f
                                                                        )
                                                                ),
                                                colors =
                                                        TextFieldDefaults.colors(
                                                                focusedContainerColor =
                                                                        Color.Transparent,
                                                                unfocusedContainerColor =
                                                                        Color.Transparent,
                                                                focusedTextColor = Color.White,
                                                                unfocusedTextColor = Color.White,
                                                                focusedIndicatorColor =
                                                                        Color.Transparent,
                                                                unfocusedIndicatorColor =
                                                                        Color.Transparent
                                                        )
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        // Action Buttons
                                        Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                                // Edit Button
                                                Button(
                                                        onClick = { onEdit(transaction) },
                                                        modifier =
                                                                Modifier.weight(1f).height(50.dp),
                                                        shape = RoundedCornerShape(12.dp),
                                                        colors =
                                                                ButtonDefaults.buttonColors(
                                                                        containerColor =
                                                                                Color.Transparent
                                                                ),
                                                        contentPadding = PaddingValues()
                                                ) {
                                                        Box(
                                                                modifier =
                                                                        Modifier.fillMaxSize()
                                                                                .background(
                                                                                        Brush.horizontalGradient(
                                                                                                listOf(
                                                                                                        PurpleGradientStart,
                                                                                                        PurpleGradientEnd
                                                                                                )
                                                                                        ),
                                                                                        RoundedCornerShape(
                                                                                                12.dp
                                                                                        )
                                                                                ),
                                                                contentAlignment = Alignment.Center
                                                        ) {
                                                                Text(
                                                                        "Edit",
                                                                        color = Color.White,
                                                                        fontWeight =
                                                                                FontWeight.SemiBold
                                                                )
                                                        }
                                                }

                                                // Delete Button
                                                Button(
                                                        onClick = {
                                                                viewModel.deleteTransaction(
                                                                        transaction
                                                                )
                                                                onBack()
                                                        },
                                                        modifier =
                                                                Modifier.weight(1f).height(50.dp),
                                                        shape = RoundedCornerShape(12.dp),
                                                        colors =
                                                                ButtonDefaults.buttonColors(
                                                                        containerColor =
                                                                                Color.Transparent
                                                                ),
                                                        contentPadding = PaddingValues()
                                                ) {
                                                        Box(
                                                                modifier =
                                                                        Modifier.fillMaxSize()
                                                                                .background(
                                                                                        Brush.horizontalGradient(
                                                                                                listOf(
                                                                                                        AccentPink,
                                                                                                        Color(
                                                                                                                0xFFFF5252
                                                                                                        )
                                                                                                )
                                                                                        ),
                                                                                        RoundedCornerShape(
                                                                                                12.dp
                                                                                        )
                                                                                ),
                                                                contentAlignment = Alignment.Center
                                                        ) {
                                                                Text(
                                                                        "Delete",
                                                                        color = Color.White,
                                                                        fontWeight =
                                                                                FontWeight.SemiBold
                                                                )
                                                        }
                                                }
                                        }
                                }
                        }
                }
        }
}
