package com.example.expensetrcaker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface WalletDao {
    @Query("SELECT * FROM wallets")
    LiveData<List<WalletEntity>> getAllWallets();

    @Insert
    void insertWallet(WalletEntity wallet);

    @Update
    void updateWallet(WalletEntity wallet);

    @Query("UPDATE wallets SET balance = balance + :amount WHERE id = :walletId")
    void updateBalance(long walletId, double amount);

    @Delete
    void deleteWallet(WalletEntity wallet);
}
