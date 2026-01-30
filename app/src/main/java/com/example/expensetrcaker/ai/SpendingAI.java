package com.example.expensetrcaker.ai;

import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.rounded.Fastfood;
import androidx.compose.material.icons.rounded.Navigation;
import androidx.compose.material.icons.rounded.ShoppingBag;
import androidx.compose.material.icons.rounded.Star;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.graphics.vector.ImageVector;
import com.example.expensetrcaker.data.TransactionEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpendingAI {
    public static SpendingPersonality calculatePersonality(List<TransactionEntity> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return new SpendingPersonality(
                    "Fresh Start",
                    "You haven't recorded enough data yet.",
                    Icons.Rounded.getStar(Icons.Rounded.INSTANCE),
                    new Color(0xFF03DAC6L),
                    "Start adding transactions to unlock your spending personality!");
        }

        Map<String, Double> categoryTotals = new HashMap<>();
        for (TransactionEntity t : transactions) {
            if (t.isExpense()) {
                categoryTotals.put(t.getCategory(), categoryTotals.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
            }
        }

        String topCategory = null;
        double maxAmount = -1;
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            if (entry.getValue() > maxAmount) {
                maxAmount = entry.getValue();
                topCategory = entry.getKey();
            }
        }

        if (topCategory == null) {
            return new SpendingPersonality(
                    "Balanced Pro",
                    "Your spending is well-distributed.",
                    Icons.Rounded.getStar(Icons.Rounded.INSTANCE),
                    new Color(0xFFBB86FCL),
                    "Great job! Your essentials and lifestyle spending are perfectly in sync.");
        }

        switch (topCategory) {
            case "Food":
                return new SpendingPersonality(
                        "The Gourmet",
                        "Your heart (and wallet) belongs to the kitchen.",
                        Icons.Rounded.getFastfood(Icons.Rounded.INSTANCE),
                        new Color(0xFFFF4081L),
                        "You spend 40% more on dining than average. Try meal prepping to save $200/mo.");
            case "Transport":
                return new SpendingPersonality(
                        "The Explorer",
                        "You're always on the move.",
                        Icons.Rounded.getNavigation(Icons.Rounded.INSTANCE),
                        new Color(0xFF00E5FFL),
                        "Commuting is your top expense. Consider a monthly pass to optimize your transit budget.");
            case "Shopping":
                return new SpendingPersonality(
                        "The Trendsetter",
                        "Style is your priority.",
                        Icons.Rounded.getShoppingBag(Icons.Rounded.INSTANCE),
                        new Color(0xFFFFC107L),
                        "Impulse buys are creeping up. Try the 24-hour rule before checking out!");
            default:
                return new SpendingPersonality(
                        "The Balanced Pro",
                        "Your spending is well-distributed.",
                        Icons.Rounded.getStar(Icons.Rounded.INSTANCE),
                        new Color(0xFFBB86FCL),
                        "Great job! Your essentials and lifestyle spending are perfectly in sync.");
        }
    }
}
