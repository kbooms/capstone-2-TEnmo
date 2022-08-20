package com.techelevator.tenmo.dao;

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
    public void sendMoney(int transferId, int fromAccountId, int toAcccountId, BigDecimal transferAmount,
                          String typeTransfer, String transferStatus ) {
        if( fromAccountId != toAcccountId && transferAmount.doubleValue() > 0 && transferAmount.doubleValue() <=    ){

        }

    }



}
