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
@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {
   AccountDao  accountDao ;


    public AccountController(AccountDao accountDao) {

        this.accountDao = accountDao;
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
     public Account getAccountByUserId(@PathVariable int userId){
     return this.accountDao.getAccount(userId);
    }

    @GetMapping
    public List<Account> getListAccount(){
        return accountDao.getListAccount();
    }

}
