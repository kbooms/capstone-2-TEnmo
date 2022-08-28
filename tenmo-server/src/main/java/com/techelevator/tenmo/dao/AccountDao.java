package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
     public Account getAccount(long userId);
     public boolean updateAccount( Account account);

     public List<Account> getListAccount();
     public Account getAccountByAccountId(long accountId);
}
