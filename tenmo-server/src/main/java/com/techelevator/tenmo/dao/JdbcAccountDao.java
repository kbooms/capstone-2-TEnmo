package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
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
    public Account getAccount(int userId){
        String sql = "Select * from account where user_id = ? ";
        Account account = new Account();
        SqlRowSet set = jdbcTemplate.queryForRowSet(sql,userId) ;

           if (set.next()) {
                  account = mapRowToAccount(set) ;
           }
        return account ;
    }

    public BigDecimal getBalance(int accountId){
        String sql = "Select balance from account where account_id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sql,BigDecimal.class, accountId);
        return balance;
    }
    @Override
    public boolean updateAccount(int userId, Account account) {
        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";
          return jdbcTemplate.update(sql, account.getBalance(), userId) == 1;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccount_id(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));

        return account;
    }


}
