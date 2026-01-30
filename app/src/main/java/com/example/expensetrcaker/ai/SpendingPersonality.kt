package com.example.expensetrcaker.ai

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class SpendingPersonality(
        val name: String,
        val description: String,
        val insight: String,
        val color: Color,
        val icon: ImageVector
)
