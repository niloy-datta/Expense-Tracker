package com.example.expensetrcaker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetrcaker.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit, onNavigate: (String) -> Unit) {
    var darkMode by remember { mutableStateOf(true) }

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
                        text = "Settings",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                )
                Spacer(modifier = Modifier.width(48.dp)) // Balance the layout
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                    contentPadding = PaddingValues(bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SettingItemToggle(
                            icon = Icons.Rounded.DarkMode,
                            title = "Dark Mode",
                            isChecked = darkMode,
                            onCheckedChange = { darkMode = it }
                    )
                }
                item {
                    SettingItemNav(
                            icon = Icons.Rounded.Category,
                            title = "Manage Categories",
                            subtitle = "Customize your expense types",
                            onClick = { onNavigate("categories") }
                    )
                }
                item {
                    SettingItemNav(
                            icon = Icons.Rounded.Language,
                            title = "Language",
                            subtitle = "English(US)"
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item { SettingItemNav(icon = Icons.Rounded.Backup, title = "Backup") }
                item { SettingItemNav(icon = Icons.Rounded.Security, title = "Privacy Data") }
                item {
                    SettingItemNav(
                            icon = Icons.Rounded.Notifications,
                            title = "Notification Preferences"
                    )
                }
                item {
                    SettingItemNav(
                            icon = Icons.Rounded.AccountBalanceWallet,
                            title = "Manage Wallets"
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item {
                    SettingItemNav(
                            icon = Icons.Rounded.AccountBalanceWallet,
                            title = "Wallets" // Duplicate for demo
                    )
                }
                item { SettingItemNav(icon = Icons.Rounded.AttachMoney, title = "Currency") }
            }
        }
    }
}

@Composable
fun SettingItemToggle(
        icon: ImageVector,
        title: String,
        isChecked: Boolean,
        onCheckedChange: (Boolean) -> Unit
) {
    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceDark.copy(alpha = 0.4f))
                            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        // Custom Toggle Switch
        Box(
                modifier =
                        Modifier.width(48.dp)
                                .height(26.dp)
                                .clip(RoundedCornerShape(13.dp))
                                .background(
                                        if (isChecked) PurpleGradientStart
                                        else Color.White.copy(alpha = 0.2f)
                                )
                                .clickable { onCheckedChange(!isChecked) }
                                .padding(3.dp),
                contentAlignment = if (isChecked) Alignment.CenterEnd else Alignment.CenterStart
        ) {
            Box(
                    modifier =
                            Modifier.size(20.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White)
            )
        }
    }
}

@Composable
fun SettingItemNav(
        icon: ImageVector,
        title: String,
        subtitle: String? = null,
        onClick: () -> Unit = {}
) {
    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceDark.copy(alpha = 0.4f))
                            .clickable(onClick = onClick)
                            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(subtitle, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                }
            }
        }

        Icon(
                Icons.Rounded.ArrowForward,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
        )
    }
}
