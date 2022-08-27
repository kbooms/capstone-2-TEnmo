package com.techelevator.tenmo.model;

public class TypeTransfer {

    private int transferTypeId;
    private String transferTypeDesc;
    public static final int ID_SEND = 2;
    public static final int ID_REQUEST = 1;
    public static final String DESC_SEND = "Send";
    public static final String DESC_REQUEST = "Request";
    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }

}
