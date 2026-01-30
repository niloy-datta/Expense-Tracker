package com.example.expensetrcaker.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "budgets")
public class BudgetEntity {
    @PrimaryKey
    @NonNull
    private String category;
    private double limitAmount;
    private String period;

    public BudgetEntity(@NonNull String category, double limitAmount, String period) {
        this.category = category;
        this.limitAmount = limitAmount;
        this.period = period;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    public double getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(double limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
