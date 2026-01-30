package com.example.expensetrcaker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector

fun getIconByName(name: String): ImageVector {
    return when (name) {
        "Fastfood" -> Icons.Rounded.Fastfood
        "DirectionsCar" -> Icons.Rounded.DirectionsCar
        "ShoppingBag" -> Icons.Rounded.ShoppingBag
        "Receipt" -> Icons.Rounded.Receipt
        "Home" -> Icons.Rounded.Home
        "Check" -> Icons.Rounded.Check
        else -> Icons.Rounded.Check
    }
}
