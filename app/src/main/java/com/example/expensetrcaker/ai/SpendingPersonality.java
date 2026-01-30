package com.example.expensetrcaker.ai;

import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.graphics.vector.ImageVector;

public class SpendingPersonality {
    private final String name;
    private final String description;
    private final ImageVector icon;
    private final Color color;
    private final String insight;

    public SpendingPersonality(String name, String description, ImageVector icon, Color color, String insight) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.color = color;
        this.insight = insight;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ImageVector getIcon() {
        return icon;
    }

    public Color getColor() {
        return color;
    }

    public String getInsight() {
        return insight;
    }
}
