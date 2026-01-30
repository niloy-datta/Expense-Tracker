package com.example.expensetrcaker.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wallets")
public class WalletEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private double balance;
    private String bank;
    private int colorStart;
    private int colorEnd;

    public WalletEntity(String name, double balance, String bank, int colorStart, int colorEnd) {
        this.name = name;
        this.balance = balance;
        this.bank = bank;
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public int getColorStart() {
        return colorStart;
    }

    public void setColorStart(int colorStart) {
        this.colorStart = colorStart;
    }

    public int getColorEnd() {
        return colorEnd;
    }

    public void setColorEnd(int colorEnd) {
        this.colorEnd = colorEnd;
    }
}
