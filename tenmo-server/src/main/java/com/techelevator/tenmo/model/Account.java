package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    private int account_id;
    private String username;
    private BigDecimal balance;

    public Account(int account_id, String username, BigDecimal balance) {
        this.account_id = account_id;
        this.username = username;
        this.balance = balance;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
