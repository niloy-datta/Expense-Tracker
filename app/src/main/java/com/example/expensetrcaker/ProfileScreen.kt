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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetrcaker.ui.theme.BackgroundDark
import com.example.expensetrcaker.ui.theme.SurfaceDark
import com.example.expensetrcaker.ui.theme.Primary

@Composable
fun ProfileScreen(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
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
                    text = "Profile",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                IconButton(onClick = { /* Settings */ }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // User Info
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(SurfaceDark)
                        .border(2.dp, Primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("N", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Niloy", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("niloy@example.com", color = Color.Gray, fontSize = 14.sp)
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Pro Badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Primary.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Primary)
                ) {
                    Text(
                        "PRO MEMBER", 
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Profile Options
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(profileOptions) { option ->
                    ProfileOptionRow(option)
                }
                
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { /* Logout */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCF6679).copy(alpha = 0.1f)),
                        border = BorderStroke(1.dp, Color(0xFFCF6679)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("LOG OUT", color = Color(0xFFCF6679), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileOptionRow(option: ProfileOption) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(option.color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(option.icon, contentDescription = null, tint = option.color, modifier = Modifier.size(20.dp))
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(option.title, color = Color.White, modifier = Modifier.weight(1f))
        
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
    }
}

data class ProfileOption(val title: String, val icon: ImageVector, val color: Color)

val profileOptions = listOf(
    ProfileOption("Account Settings", Icons.Rounded.Person, Color(0xFF6C63FF)),
    ProfileOption("Export Data (CSV/PDF)", Icons.Rounded.IosShare, Color(0xFF03DAC6)),
    ProfileOption("Shared Wallets", Icons.Rounded.Group, Color(0xFFFFC107)),
    ProfileOption("Theme & Appearance", Icons.Rounded.Palette, Color(0xFF9C27B0)),
    ProfileOption("Security & FaceID", Icons.Rounded.VerifiedUser, Color(0xFF00E5FF))
)
