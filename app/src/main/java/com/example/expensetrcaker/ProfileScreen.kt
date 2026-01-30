package com.example.expensetrcaker

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.expensetrcaker.ui.theme.*
import com.example.expensetrcaker.viewmodel.TransactionViewModel

@Composable
fun ProfileScreen(onBack: () -> Unit, viewModel: TransactionViewModel? = null) {
    val transactions by
            viewModel?.allTransactions?.observeAsState(initial = emptyList())
                    ?: remember { mutableStateOf(emptyList()) }
    val categories by
            viewModel?.allCategories?.observeAsState(initial = emptyList())
                    ?: remember { mutableStateOf(emptyList()) }
    val balance by
            viewModel?.totalBalance?.observeAsState(initial = 0.0)
                    ?: remember { mutableStateOf(0.0) }

    Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        LazyColumn(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
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
                            text = "Profile",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            modifier = Modifier.weight(1f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    IconButton(onClick = { /* Settings */}) {
                        Icon(
                                Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color.White
                        )
                    }
                }
            }

            // User Info with Glowing Avatar
            item {
                Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                            modifier =
                                    Modifier.size(110.dp)
                                            .shadow(
                                                    elevation = 24.dp,
                                                    spotColor = Primary.copy(alpha = 0.6f),
                                                    shape = CircleShape
                                            )
                                            .clip(CircleShape)
                                            .background(SurfaceDark)
                                            .border(
                                                    width = 3.dp,
                                                    brush =
                                                            Brush.linearGradient(
                                                                    colors =
                                                                            listOf(
                                                                                    PurpleGradientStart,
                                                                                    PurpleGradientEnd
                                                                            )
                                                            ),
                                                    shape = CircleShape
                                            ),
                            contentAlignment = Alignment.Center
                    ) {
                        Text(
                                "N",
                                color = Color.White,
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                            "Niloy",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                    )
                    Text("niloy@example.com", color = Color.Gray, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Pro Badge with Glow
                    Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = Primary.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Primary),
                            modifier =
                                    Modifier.shadow(
                                            8.dp,
                                            RoundedCornerShape(24.dp),
                                            spotColor = Primary.copy(0.5f)
                                    )
                    ) {
                        Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                    Icons.Rounded.Star,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(16.dp)
                            )
                            Text(
                                    "PRO MEMBER",
                                    color = Primary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Stats Cards
            item {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatsCard(
                            icon = Icons.Rounded.Receipt,
                            value = "${transactions.size}",
                            label = "Transactions",
                            color = AccentPink,
                            modifier = Modifier.weight(1f)
                    )
                    StatsCard(
                            icon = Icons.Rounded.Category,
                            value = "${categories.size}",
                            label = "Categories",
                            color = AccentGreen,
                            modifier = Modifier.weight(1f)
                    )
                    StatsCard(
                            icon = Icons.Rounded.AccountBalanceWallet,
                            value = "$${"%,.0f".format(balance)}",
                            label = "Balance",
                            color = Primary,
                            modifier = Modifier.weight(1f)
                    )
                }
            }

            // Section Label
            item {
                Text(
                        "Settings & Preferences",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Profile Options
            items(profileOptions) { option -> ProfileOptionRow(option) }

            // Logout Button
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                        onClick = { /* Logout */},
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors =
                                ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFCF6679).copy(alpha = 0.1f)
                                ),
                        border = BorderStroke(1.5.dp, Color(0xFFCF6679)),
                        shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                                Icons.Rounded.Logout,
                                contentDescription = null,
                                tint = Color(0xFFCF6679)
                        )
                        Text(
                                "LOG OUT",
                                color = Color(0xFFCF6679),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(80.dp)) // Bottom padding for nav bar
            }
        }
    }
}

@Composable
fun StatsCard(
        icon: ImageVector,
        value: String,
        label: String,
        color: Color,
        modifier: Modifier = Modifier
) {
    Box(
            modifier =
                    modifier.height(100.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                    Brush.linearGradient(
                                            colors =
                                                    listOf(
                                                            SurfaceDark.copy(alpha = 0.8f),
                                                            SurfaceDark.copy(alpha = 0.4f)
                                                    )
                                    )
                            )
                            .border(
                                    width = 1.dp,
                                    color = color.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(20.dp)
                            )
                            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
            Box(
                    modifier =
                            Modifier.size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }

            Column {
                Text(value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(label, color = Color.Gray, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun ProfileOptionRow(option: ProfileOption) {
    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceDark.copy(alpha = 0.6f))
                            .border(
                                    width = 1.dp,
                                    color = Color.White.copy(alpha = 0.05f),
                                    shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {}
                            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
                modifier =
                        Modifier.size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(option.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
        ) {
            Icon(
                    option.icon,
                    contentDescription = null,
                    tint = option.color,
                    modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
                option.title,
                color = Color.White,
                modifier = Modifier.weight(1f),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
        )

        Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray.copy(alpha = 0.6f)
        )
    }
}

data class ProfileOption(val title: String, val icon: ImageVector, val color: Color)

val profileOptions =
        listOf(
                ProfileOption("Account Settings", Icons.Rounded.Person, Color(0xFF6C63FF)),
                ProfileOption("Notifications", Icons.Rounded.Notifications, Color(0xFF03DAC6)),
                ProfileOption("Export Data (CSV/PDF)", Icons.Rounded.IosShare, Color(0xFF00E676)),
                ProfileOption("Currency & Language", Icons.Rounded.Language, Color(0xFFFFC107)),
                ProfileOption("Privacy & Security", Icons.Rounded.Lock, Color(0xFF00E5FF)),
                ProfileOption("Theme & Appearance", Icons.Rounded.Palette, Color(0xFF9C27B0)),
                ProfileOption("Help & Support", Icons.Rounded.Help, Color(0xFFFF6E40)),
                ProfileOption("About App", Icons.Rounded.Info, Color(0xFF8A8A8E))
        )
