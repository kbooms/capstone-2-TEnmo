package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface TransferDao {
    public void sendMoney(int transferId, int fromAccountId, int toAcccountId, BigDecimal transferAmount,
                          String typeTransfer, String transferStatus );

}
