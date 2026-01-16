package com.example.expensetrcaker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "wallets")
data class WalletEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val balance: Double,
    val bank: String,
    val colorStart: Int, // Store as Int ARGB
    val colorEnd: Int
)

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallets")
    fun getAllWallets(): Flow<List<WalletEntity>>

    @Insert
    suspend fun insertWallet(wallet: WalletEntity)

    @Update
    suspend fun updateWallet(wallet: WalletEntity)

    @Query("UPDATE wallets SET balance = balance + :amount WHERE id = :walletId")
    suspend fun updateBalance(walletId: Long, amount: Double)

    @Delete
    suspend fun deleteWallet(wallet: WalletEntity)
}
