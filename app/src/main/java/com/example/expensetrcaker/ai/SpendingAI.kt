package com.example.expensetrcaker.ai

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.ui.graphics.Color
import com.example.expensetrcaker.data.TransactionEntity

object SpendingAI {
    @JvmStatic
    fun calculatePersonality(transactions: List<TransactionEntity>): SpendingPersonality {
        if (transactions.isEmpty()) {
            return SpendingPersonality(
                    name = "Newcomer",
                    description = "Start tracking to unlock insights",
                    insight = "Your financial journey begins with a single step.",
                    color = Color.Gray,
                    icon = Icons.Default.Info
            )
        }

        val expenses = transactions.filter { it.isExpense() }

        if (expenses.isEmpty()) {
            return SpendingPersonality(
                    name = "Saver",
                    description = "You are mindful of your spending.",
                    insight = "Great job! Keep building that emergency fund.",
                    color = Color(0xFF00E676),
                    icon = Icons.Default.Savings
            )
        }

        val categoryTotals =
                expenses.groupBy { it.getCategory() }.mapValues { entry ->
                    entry.value.sumOf { it.getAmount() }
                }

        val topCategory = categoryTotals.maxByOrNull { it.value }?.key ?: "General"

        return when (topCategory) {
            "Food" ->
                    SpendingPersonality(
                            name = "Foodie",
                            description = "You love good food!",
                            insight = "Consider cooking at home more often to save.",
                            color = Color(0xFFFF4081),
                            icon = Icons.Default.Star
                    )
            "Shopping" ->
                    SpendingPersonality(
                            name = "Shopaholic",
                            description = "Retail therapy is your go-to.",
                            insight = "Try the 24-hour rule before buying non-essentials.",
                            color = Color(0xFFFFC107),
                            icon = Icons.Default.ShoppingCart
                    )
            "Transport" ->
                    SpendingPersonality(
                            name = "Traveler",
                            description = "Always on the move.",
                            insight = "Look for commute passes or carpooling options.",
                            color = Color(0xFF00E5FF),
                            icon = Icons.Default.TrendingUp
                    )
            else ->
                    SpendingPersonality(
                            name = "Saver",
                            description = "You are mindful of your spending.",
                            insight = "Great job! Keep building that emergency fund.",
                            color = Color(0xFF00E676),
                            icon = Icons.Default.Savings
                    )
        }
    }
}
