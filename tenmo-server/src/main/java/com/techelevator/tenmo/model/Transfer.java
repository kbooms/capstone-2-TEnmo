package com.techelevator.tenmo.model;

import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

public class Transfer {

    private int transferId;

    private String fromAccount;

    private String toAccount;

    private String typeTransfer;
    private String transferStatus;
    private BigDecimal amountForTransfer;

    public Transfer(int transferId, String fromAccount, String toAccount, String typeTransfer, String transferStatus, BigDecimal amountForTransfer) {
        this.transferId = transferId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.typeTransfer = typeTransfer;
        this.transferStatus = transferStatus;
        this.amountForTransfer = amountForTransfer;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getTypeTransfer() {
        return typeTransfer;
    }

    public void setTypeTransfer(String typeTransfer) {
        this.typeTransfer = typeTransfer;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public BigDecimal getAmountForTransfer() {
        return amountForTransfer;
    }

    public void setAmountForTransfer(BigDecimal amountForTransfer) {
        this.amountForTransfer = amountForTransfer;
    }
}
