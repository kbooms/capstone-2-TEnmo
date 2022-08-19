package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RequestMapping("/accounts")
//@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {
  AccountDao  accountDao ;
    @RequestMapping(value = "/balance/{userId}", method = RequestMethod.GET)
private BigDecimal getBalance(@PathVariable int userId){
    return accountDao.getBalance(userId);
}

}
