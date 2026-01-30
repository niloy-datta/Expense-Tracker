package com.example.expensetrcaker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface BudgetDao {
    @Query("SELECT * FROM budgets")
    LiveData<List<BudgetEntity>> getAllBudgets();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsertBudget(BudgetEntity budget);

    @Query("DELETE FROM budgets WHERE category = :category")
    void deleteBudget(String category);
}
