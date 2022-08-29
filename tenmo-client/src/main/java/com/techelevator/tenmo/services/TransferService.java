package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TransferService {
    public  String api_base_url ;
    private RestTemplate restTemplate = new RestTemplate();
    private String token;

    public TransferService(String api_base_url, String token) {
        this.api_base_url = api_base_url + "transfers";
        this.token = token;
    }
    private HttpEntity<Account> getAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(account, headers);
    }
    private HttpEntity<Transfer> getTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>( transfer, headers);
    }


    public Transfer[] getAllTransfers(Account account){
        Transfer[] transfers = new Transfer[0];
        try {
            transfers= restTemplate.exchange(api_base_url, HttpMethod.GET, getAccountEntity(account),Transfer[].class).getBody();
            return transfers;

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public Transfer[] getSentTransfers(Account account){
        Transfer[] transfers = null;
        try {
            transfers= restTemplate.exchange(api_base_url +"/listSent", HttpMethod.POST, getAccountEntity(account),Transfer[].class).getBody();

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public Transfer[] getRequestTransfers(Account account){
        Transfer[] transfers = new Transfer[0];
        try {
            transfers= restTemplate.exchange(api_base_url + "/listRequest", HttpMethod.POST, getAccountEntity(account),Transfer[].class).getBody();
            return transfers;

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }




    public void updateStatus(Transfer transfer){
        int statusCode =0;
        try {
            statusCode = restTemplate.exchange(api_base_url, HttpMethod.PUT,getTransferEntity(transfer),boolean.class).getStatusCodeValue();

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        if (statusCode ==HttpStatus.NO_CONTENT.value())
            System.out.println("request "+ transfer.getTransferId()
                + " "+ transfer.getTransferStatus().getTransferStatusDesc());
    }

    public void sendMoney(Transfer transfer){
        int statusCode =0;
        try {
              statusCode= restTemplate.exchange(api_base_url + "/send", HttpMethod.POST,getTransferEntity(transfer),boolean.class).getStatusCodeValue();

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        if (statusCode ==HttpStatus.CREATED.value()) System.out.println("Money sent to " + transfer.getToAccount().getUser().getUsername());
    }
    public void requestMoney(Transfer transfer){
        int statusCode =0;
        try {
            statusCode=  restTemplate.exchange(api_base_url + "/request", HttpMethod.POST,getTransferEntity(transfer),boolean.class).getStatusCodeValue();

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        if (statusCode ==HttpStatus.CREATED.value()) System.out.println("Money Request to "+transfer.getToAccount().getUser().getUsername()+" created Successfully");
    }

}
