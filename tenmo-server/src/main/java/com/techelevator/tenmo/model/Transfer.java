package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferId;

    private Account fromAccount;

    private Account toAccount;

    private TypeTransfer typeTransfer;
    private StatusTransfer transferStatus;
    private BigDecimal amountForTransfer;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public TypeTransfer getTypeTransfer() {
        return typeTransfer;
    }

    public void setTypeTransfer(TypeTransfer typeTransfer) {
        this.typeTransfer = typeTransfer;
    }

    public StatusTransfer getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(StatusTransfer transferStatus) {
        this.transferStatus = transferStatus;
    }

    public BigDecimal getAmountForTransfer() {
        return amountForTransfer;
    }

    public void setAmountForTransfer(BigDecimal amountForTransfer) {
        this.amountForTransfer = amountForTransfer;
    }
}
