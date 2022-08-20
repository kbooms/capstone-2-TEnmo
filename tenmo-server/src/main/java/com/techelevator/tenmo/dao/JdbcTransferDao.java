package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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



}
