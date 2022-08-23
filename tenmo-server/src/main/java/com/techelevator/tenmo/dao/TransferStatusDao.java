package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.StatusTransfer;

public interface TransferStatusDao {


    StatusTransfer getStatus(int statusId);

}
