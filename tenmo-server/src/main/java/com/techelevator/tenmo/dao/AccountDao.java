package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
     public BigDecimal getBalance(int accountId);
     public Account getAccount(int userId);

     public boolean updateAccount(int userId, Account account);
}
