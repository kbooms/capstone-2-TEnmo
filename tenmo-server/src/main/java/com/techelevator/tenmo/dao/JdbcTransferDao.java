package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public boolean sendMoney(Transfer transfer) {
        String sql = "INSERT into TRANSFER(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "values(?, ?, ?, ?)";
        return jdbcTemplate.update(sql, transfer.getTransferId(), transfer.getTypeTransfer(), transfer.getFromAccount(),
                            transfer.getToAccount(), transfer.getAmountForTransfer() ) == 1;

    }

     public List<Transfer> transfersTo(Account account){
         List<Transfer> transfers = new ArrayList<>();
         String sql = "SELECT transfer.transfer_id, tenmo_user.username as username_from, transfer.amount, transfer_status.transfer_type_desc \n" +
                 "From transfer JOIN transfer_type on transfer_type.transfer_type_id = transfer.transfer_type_id\n" +
                 "JOIN transfer_status ON transfer_status.transfer_status_id = transfer.transfer_status_id\n" +
                 "JOIN account ON account.account_id = transfer.account_from    \n" +
                 "JOIN tenmo_user on tenmo_user.user_id = account.user_id"
                 + " where account.account_id = ?";
         SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,account.getAccount_id());
         while (rowSet.next()){
             Transfer transfer = mapRowToTransfer(rowSet);
             transfers.add(transfer);
         }

         return transfers;
     }
    @Override
    public List<Transfer> transfersFrom(Account account) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer.transfer_id, tenmo_user.username as username_to, transfer.amount, transfer_status.transfer_type_desc \n" +
                "From transfer JOIN transfer_type on transfer_type.transfer_type_id = transfer.transfer_type_id\n" +
                "JOIN transfer_status ON transfer_status.transfer_status_id = transfer.transfer_status_id\n" +
                "JOIN account ON account.account_id = transfer.account_from OR account.account_id = transfer.account_to  \n" +
                "JOIN tenmo_user on tenmo_user.user_id = account.user_id"
                + " where account.account_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,account.getAccount_id()); // make from list and to list



    // for loop through from list .setAccountFrom
                                                                                      //for loop through toList . setAccountTo
                                                                                       //
        while (rowSet.next()){
            Transfer transfer = mapRowToTransfer(rowSet);
            transfers.add(transfer);
        }
        
        return transfers;


    }
    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferStatus(rs.getString("transfer_status_desc"));
        transfer.setFromAccount(rs.getString("username_from"));
        transfer.setToAccount(rs.getString("username_to"));
        transfer.setAmountForTransfer(rs.getBigDecimal("amount"));

        return transfer;
    }

}
