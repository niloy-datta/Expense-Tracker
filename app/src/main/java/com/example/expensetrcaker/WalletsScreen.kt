package com.example.expensetrcaker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetrcaker.ui.theme.BackgroundDark
import com.example.expensetrcaker.ui.theme.Primary
import com.example.expensetrcaker.ui.theme.SurfaceDark
import com.example.expensetrcaker.viewmodel.TransactionViewModel

@Composable
fun WalletsScreen(onBack: () -> Unit, viewModel: TransactionViewModel) {
    val wallets by viewModel.allWallets.observeAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }

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
                        text = "My Wallets",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                IconButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Rounded.Add, contentDescription = "Add", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (wallets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No wallets found. Tap + to add.", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    items(wallets) { wallet ->
                        WalletCard(
                                WalletData(
                                        name = wallet.getName(),
                                        balance = "$${"%,.2f".format(wallet.getBalance())}",
                                        colors =
                                                listOf(
                                                        Color(wallet.getColorStart()),
                                                        Color(wallet.getColorEnd())
                                                ),
                                        bank = wallet.getBank()
                                )
                        )
                    }
                }
            }

            if (showAddDialog) {
                AddWalletDialog(
                        onDismiss = { showAddDialog = false },
                        onSave = { name, bal, bankName ->
                            viewModel.addWallet(name, bal, bankName, "#8E2DE2", "#4A00E0")
                            showAddDialog = false
                        }
                )
            }
        }
    }
}

@Composable
fun AddWalletDialog(onDismiss: () -> Unit, onSave: (String, Double, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var bankName by remember { mutableStateOf("") }

    AlertDialog(
            onDismissRequest = onDismiss,
            containerColor = SurfaceDark,
            title = { Text("Add New Wallet", color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("Wallet Name (e.g. Savings)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors =
                                    TextFieldDefaults.colors(
                                            focusedContainerColor = BackgroundDark,
                                            unfocusedContainerColor = BackgroundDark,
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White
                                    )
                    )
                    TextField(
                            value = balance,
                            onValueChange = { balance = it },
                            placeholder = { Text("Initial Balance") },
                            modifier = Modifier.fillMaxWidth(),
                            colors =
                                    TextFieldDefaults.colors(
                                            focusedContainerColor = BackgroundDark,
                                            unfocusedContainerColor = BackgroundDark,
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White
                                    )
                    )
                    TextField(
                            value = bankName,
                            onValueChange = { bankName = it },
                            placeholder = { Text("Bank/Type (e.g. Visa)") },
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
                            val bal = balance.toDoubleOrNull() ?: 0.0
                            if (name.isNotBlank()) onSave(name, bal, bankName)
                        }
                ) { Text("ADD WALLET", color = Primary) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text("CANCEL", color = Color.Gray) }
            }
    )
}

@Composable
fun WalletCard(wallet: WalletData) {
    Box(
            modifier =
                    Modifier.fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Brush.linearGradient(wallet.colors))
                            .padding(24.dp)
    ) {
        Column {
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                        Icons.Rounded.AccountBalanceWallet,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f)
                )
                Text(wallet.bank, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(wallet.name, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            Text(
                    wallet.balance,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
            )
        }
    }
}

data class WalletData(
        val name: String,
        val balance: String,
        val colors: List<Color>,
        val bank: String
)
