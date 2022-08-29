package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    public boolean Create(Transfer transfer);
    public List<Transfer> getTransfersList(Account account);
    public boolean updateStatus(Transfer transfer);
    public List<Transfer> approvedTransferList(Account account);
    public List<Transfer> pendingTransferList(Account account);


}
