package com.example.expensetrcaker.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = { TransactionEntity.class, BudgetEntity.class, WalletEntity.class,
        CategoryEntity.class }, version = 5)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TransactionDao transactionDao();

    public abstract BudgetDao budgetDao();

    public abstract WalletDao walletDao();

    public abstract CategoryDao categoryDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "expense_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
