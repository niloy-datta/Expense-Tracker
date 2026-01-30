package com.example.expensetrcaker.viewmodel;

import android.app.Application;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.example.expensetrcaker.ai.SpendingAI;
import com.example.expensetrcaker.ai.SpendingPersonality;
import com.example.expensetrcaker.data.AppDatabase;
import com.example.expensetrcaker.data.BudgetDao;
import com.example.expensetrcaker.data.BudgetEntity;
import com.example.expensetrcaker.data.TransactionDao;
import com.example.expensetrcaker.data.TransactionEntity;
import com.example.expensetrcaker.data.WalletDao;
import com.example.expensetrcaker.data.WalletEntity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionViewModel extends AndroidViewModel {
    private final TransactionDao transactionDao;
    private final BudgetDao budgetDao;
    private final WalletDao walletDao;
    private final ExecutorService executorService;

    private final LiveData<List<TransactionEntity>> allTransactions;
    private final LiveData<List<BudgetEntity>> allBudgets;
    private final LiveData<List<WalletEntity>> allWallets;
    private final LiveData<Double> totalIncome;
    private final LiveData<Double> totalExpense;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        transactionDao = db.transactionDao();
        budgetDao = db.budgetDao();
        walletDao = db.walletDao();
        executorService = Executors.newSingleThreadExecutor();

        allTransactions = transactionDao.getAllTransactions();
        allBudgets = budgetDao.getAllBudgets();
        allWallets = walletDao.getAllWallets();
        totalIncome = transactionDao.getTotalIncome();
        totalExpense = transactionDao.getTotalExpense();

        // Initialize default wallet if empty
        allWallets.observeForever(wallets -> {
            if (wallets != null && wallets.isEmpty()) {
                executorService.execute(() -> {
                    walletDao.insertWallet(new WalletEntity(
                            "Main Wallet",
                            5000.0,
                            "Mastercard",
                            Color.parseColor("#8E2DE2"),
                            Color.parseColor("#4A00E0")));
                });
            }
        });
    }

    public LiveData<List<TransactionEntity>> getAllTransactions() {
        return allTransactions;
    }

    public LiveData<List<BudgetEntity>> getAllBudgets() {
        return allBudgets;
    }

    public LiveData<List<WalletEntity>> getAllWallets() {
        return allWallets;
    }

    public LiveData<Double> getTotalIncome() {
        return Transformations.map(totalIncome, val -> val != null ? val : 0.0);
    }

    public LiveData<Double> getTotalExpense() {
        return Transformations.map(totalExpense, val -> val != null ? val : 0.0);
    }

    public LiveData<Double> getTotalBalance() {
        return Transformations.map(allWallets, wallets -> {
            double sum = 0;
            if (wallets != null) {
                for (WalletEntity w : wallets)
                    sum += w.getBalance();
            }
            return sum;
        });
    }

    public LiveData<SpendingPersonality> getPersonality() {
        return Transformations.map(allTransactions, transactions -> SpendingAI.calculatePersonality(transactions));
    }

    public void addTransaction(String title, double amount, String category, boolean isExpense, long walletId) {
        executorService.execute(() -> {
            transactionDao.insertTransaction(new TransactionEntity(
                    title, amount, category, System.currentTimeMillis(), isExpense, walletId));
            double balanceChange = isExpense ? -amount : amount;
            walletDao.updateBalance(walletId, balanceChange);
        });
    }

    public void deleteTransaction(TransactionEntity transaction) {
        executorService.execute(() -> {
            transactionDao.deleteTransaction(transaction);
            double balanceChange = transaction.isExpense() ? transaction.getAmount() : -transaction.getAmount();
            walletDao.updateBalance(transaction.getWalletId(), balanceChange);
        });
    }

    public void upsertBudget(String category, double limit) {
        executorService.execute(() -> {
            budgetDao.upsertBudget(new BudgetEntity(category, limit, "Monthly"));
        });
    }

    public void addWallet(String name, double balance, String bank, String colorStart, String colorEnd) {
        executorService.execute(() -> {
            walletDao.insertWallet(new WalletEntity(
                    name, balance, bank, Color.parseColor(colorStart), Color.parseColor(colorEnd)));
        });
    }

    public void deleteWallet(WalletEntity wallet) {
        executorService.execute(() -> {
            walletDao.deleteWallet(wallet);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
