package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
@Component

public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;



    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public BigDecimal getBalance(int accountId) {
        String sql = "Select balance from account where account_id = ? ";
        Account account =  jdbcTemplate.queryForObject(sql,Account.class,accountId);

        return account.getBalance();
    }
}
