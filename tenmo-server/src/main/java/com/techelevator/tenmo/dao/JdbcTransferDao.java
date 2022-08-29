package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TypeTransfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;
    private JdbcAccountDao accountDao;
    private  jdbcTransferStatus statusDao;
    private jdbcTransferType typeDao;


    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        accountDao = new JdbcAccountDao(jdbcTemplate);
        statusDao = new jdbcTransferStatus(jdbcTemplate);
        typeDao = new jdbcTransferType(jdbcTemplate);

    }


    @Override
    public boolean Create(Transfer transfer) {
        String sql = "INSERT into TRANSFER(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "values(?, ?, ?, ?,?)";
        return jdbcTemplate.update(sql,
                transfer.getTypeTransfer().getTransferTypeId(),
                transfer.getTransferStatus().getTransferStatusId(),
                transfer.getFromAccount().getAccount_id(),
                transfer.getToAccount().getAccount_id(),
                transfer.getAmountForTransfer() ) == 1;
    }

    @Override
     public List<Transfer> getTransfersList(Account account){
         List<Transfer> transfers = new ArrayList<>();
         String sql = "Select * from transfer where account_from = ? or account_to = ? ";
         SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,account.getAccount_id(),account.getAccount_id() );
         while (rowSet.next()){
            Transfer transfer = mapRowToTransfer(rowSet);
             transfers.add(transfer);
             System.out.println(account.getAccount_id());
         }

         return transfers;
     }

    @Override
    public boolean updateStatus(Transfer transfer) {
        String sql = "Update transfer set transfer_status_id = ? where transfer_id = ?";
        return jdbcTemplate.update(sql, transfer.getTransferStatus().getTransferStatusId(),
                transfer.getTransferId()) == 1;
    }

    @Override
    public List<Transfer> approvedTransferList(Account account) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "Select * from transfer where (account_from = ? or account_to = ?) and transfer_status_id > 1";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,account.getAccount_id(),account.getAccount_id() );
        while (rowSet.next()){
            Transfer transfer = mapRowToTransfer(rowSet);
            transfers.add(transfer);
            System.out.println(account.getAccount_id());
        }

        return transfers;
    }

    @Override
    public List<Transfer> pendingTransferList(Account account) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "Select * from transfer where account_to = ?  " +
                "and transfer_status_id = 1 and transfer_type_id = 1 ";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,account.getAccount_id() );
        while (rowSet.next()){
            Transfer transfer = mapRowToTransfer(rowSet);
            transfers.add(transfer);
            System.out.println(account.getAccount_id());
        }

        return transfers;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setFromAccount(accountDao.getAccountByAccountId(rs.getInt("account_from")));
        transfer.setToAccount(accountDao.getAccountByAccountId(rs.getInt("account_to")));

        transfer.setTransferStatus(statusDao.getStatus(rs.getInt("transfer_status_id")));
        transfer.setTypeTransfer(typeDao.getType(rs.getInt("transfer_type_id")));
        transfer.setAmountForTransfer(rs.getBigDecimal("amount"));

        return transfer;
    }


}
