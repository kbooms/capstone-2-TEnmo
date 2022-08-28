package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component

public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;
    private JdbcUserDao userDao;



    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        userDao = new JdbcUserDao(jdbcTemplate);
    }



    @Override
    public Account getAccount(long userId){
        String sql = "Select balance from account where account.user_id = ? ";
        Account account = getAccountWithOutBalance(userId);
        BigDecimal balance = jdbcTemplate.queryForObject(sql,BigDecimal.class,userId) ;
        account.setBalance(balance);
        return account ;
    }



    @Override
    public boolean updateAccount(Account account) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
          return jdbcTemplate.update(sql, account.getBalance(), account.getAccount_id()) == 1;
    }

    @Override
    public List<Account> getListAccount() {
        String sql ="Select account.user_id, username, balance , account_id" +
                "  from account join tenmo_user on tenmo_user.user_id = account.user_id  ";
         List<Account> list = new ArrayList<>();
         SqlRowSet set = jdbcTemplate.queryForRowSet(sql);
         while(set.next()){
             list.add(mapRowToAccount(set));
         }
         return list;

    }

    public Account getAccountWithOutBalance(long userId){
        String sql = "Select account.user_id, username, balance , account_id" +
                "  from account join tenmo_user on tenmo_user.user_id = account.user_id  " +
                " where account.user_id = ? ";
        Account account = new Account();
        SqlRowSet set = jdbcTemplate.queryForRowSet(sql,userId) ;

        if (set.next()) {
            account = mapRowToAccount(set) ;
        }

        return account ;


    }
    @Override
    public Account getAccountByAccountId(long accountId){
        String sql = "Select account.user_id, username, balance , account_id" +
                "  from account join tenmo_user on tenmo_user.user_id = account.user_id  " +
                " where account.account_id = ? ";
        Account account = new Account();
        SqlRowSet set = jdbcTemplate.queryForRowSet(sql,accountId) ;

        if (set.next()) {
            account = mapRowToAccount(set) ;
        }

        return account ;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccount_id(rs.getInt("account_id"));

        User user = userDao.findByUsername(rs.getString("username"));
        UserDetails userDetails = new UserDetails();
        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUsername());
        account.setUser(userDetails);

        return account;
    }


}
