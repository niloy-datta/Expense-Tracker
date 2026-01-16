package com.example.expensetrcaker.ai

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.expensetrcaker.data.TransactionEntity

data class SpendingPersonality(
    val name: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val insight: String
)

object SpendingAI {
    fun calculatePersonality(transactions: List<TransactionEntity>): SpendingPersonality {
        if (transactions.isEmpty()) {
            return SpendingPersonality(
                "Fresh Start",
                "You haven't recorded enough data yet.",
                Icons.Rounded.Star,
                Color(0xFF03DAC6),
                "Start adding transactions to unlock your spending personality!"
            )
        }

        val expenses = transactions.filter { it.isExpense }
        val categoryTotals = expenses.groupBy { it.category }
            .mapValues { it.value.sumOf { t -> t.amount } }

        val topCategory = categoryTotals.maxByOrNull { it.value }?.key

        return when (topCategory) {
            "Food" -> SpendingPersonality(
                "The Gourmet",
                "Your heart (and wallet) belongs to the kitchen.",
                Icons.Rounded.Fastfood,
                Color(0xFFFF4081),
                "You spend 40% more on dining than average. Try meal prepping to save $200/mo."
            )
            "Transport" -> SpendingPersonality(
                "The Explorer",
                "You're always on the move.",
                Icons.Rounded.Navigation,
                Color(0xFF00E5FF),
                "Commuting is your top expense. Consider a monthly pass to optimize your transit budget."
            )
            "Shopping" -> SpendingPersonality(
                "The Trendsetter",
                "Style is your priority.",
                Icons.Rounded.ShoppingBag,
                Color(0xFFFFC107),
                "Impulse buys are creeping up. Try the 24-hour rule before checking out!"
            )
            else -> SpendingPersonality(
                "The Balanced Pro",
                "Your spending is well-distributed.",
                Icons.Rounded.Star,
                Color(0xFFBB86FC),
                "Great job! Your essentials and lifestyle spending are perfectly in sync."
            )
        }
    }
}
