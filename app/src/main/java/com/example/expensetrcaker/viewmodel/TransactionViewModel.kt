package com.example.expensetrcaker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrcaker.data.AppDatabase
import com.example.expensetrcaker.data.BudgetEntity
import com.example.expensetrcaker.data.TransactionEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).transactionDao()

    val allTransactions: StateFlow<List<TransactionEntity>> = dao.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalIncome: StateFlow<Double> = dao.getTotalIncome()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpense: StateFlow<Double> = dao.getTotalExpense()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val balance: StateFlow<Double> = totalIncome.map { it } // Simplified for now
        // Usually balance = income - expense
        // But we'll handle the math in the UI or here.
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val budgetDao = AppDatabase.getDatabase(application).budgetDao()
    val allBudgets = budgetDao.getAllBudgets().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val walletDao = AppDatabase.getDatabase(application).walletDao()
    val allWallets = walletDao.getAllWallets().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val totalBalance = allWallets.map { wallets ->
        wallets.sumOf { it.balance }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    val personality = allTransactions.map { transactions ->
        com.example.expensetrcaker.ai.SpendingAI.calculatePersonality(transactions)
    }.stateIn(viewModelScope, SharingStarted.Lazily, com.example.expensetrcaker.ai.SpendingAI.calculatePersonality(emptyList()))

    init {
        // Initialize common wallets if empty
        viewModelScope.launch {
            walletDao.getAllWallets().collect {
                if (it.isEmpty()) {
                    walletDao.insertWallet(com.example.expensetrcaker.data.WalletEntity(
                        name = "Main Wallet",
                        balance = 5000.0,
                        bank = "Mastercard",
                        colorStart = android.graphics.Color.parseColor("#8E2DE2"),
                        colorEnd = android.graphics.Color.parseColor("#4A00E0")
                    ))
                }
            }
        }
    }

    fun addTransaction(title: String, amount: Double, category: String, isExpense: Boolean, walletId: Long = 1) {
        viewModelScope.launch {
            dao.insertTransaction(
                com.example.expensetrcaker.data.TransactionEntity(
                    title = title,
                    amount = amount,
                    date = System.currentTimeMillis(),
                    category = category,
                    isExpense = isExpense,
                    walletId = walletId
                )
            )
            // Update Wallet Balance
            val balanceChange = if (isExpense) -amount else amount
            walletDao.updateBalance(walletId, balanceChange)
        }
    }

    fun deleteTransaction(transaction: com.example.expensetrcaker.data.TransactionEntity) {
        viewModelScope.launch {
            dao.deleteTransaction(transaction)
            // Reverse Wallet Balance
            val balanceChange = if (transaction.isExpense) transaction.amount else -transaction.amount
            walletDao.updateBalance(transaction.walletId, balanceChange)
        }
    }

    fun upsertBudget(category: String, limit: Double) {
        viewModelScope.launch {
            budgetDao.upsertBudget(com.example.expensetrcaker.data.BudgetEntity(category, limit))
        }
    }

    fun addWallet(name: String, balance: Double, bank: String, colorStart: String, colorEnd: String) {
        viewModelScope.launch {
            walletDao.insertWallet(com.example.expensetrcaker.data.WalletEntity(
                name = name,
                balance = balance,
                bank = bank,
                colorStart = android.graphics.Color.parseColor(colorStart),
                colorEnd = android.graphics.Color.parseColor(colorEnd)
            ))
        }
    }

    fun deleteWallet(wallet: com.example.expensetrcaker.data.WalletEntity) {
        viewModelScope.launch {
            walletDao.deleteWallet(wallet)
        }
    }
}
