package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/accounts")
//@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {
  AccountDao  accountDao ;


    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
     private Account getAccount(@PathVariable int userId){
     return accountDao.getAccount(userId);
    }

    @GetMapping
    private List<Account> getListAccount(){
        return accountDao.getListAccount();
    }

}
