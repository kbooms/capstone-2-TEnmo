package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    public  String api_base_url ;
    private RestTemplate restTemplate = new RestTemplate();
    private String token;

    public TransferService(String api_base_url, String token) {
        this.api_base_url = api_base_url;
        this.token = token;
    }
    private HttpEntity<Account> getAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>( account, headers);
    }
    private HttpEntity<Transfer> getTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>( transfer, headers);
    }


    public Transfer[] getAllTransfers(Account account){
        Transfer [] transfers = null;
        try {
            return  restTemplate.exchange(api_base_url, HttpMethod.GET, getAccountEntity(account),Transfer[].class).getBody();

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public boolean updateStatus(Transfer transfer){
        boolean isUpdated = false;
        try {
            isUpdated =  restTemplate.exchange(api_base_url, HttpMethod.PUT,getTransferEntity(transfer),boolean.class).getBody();

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return isUpdated;
    }

    public boolean sendMoney(Transfer transfer){
        boolean isSent = false;
        try {
            isSent =  restTemplate.exchange(api_base_url + "/send", HttpMethod.POST,getTransferEntity(transfer),boolean.class).getBody();

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return isSent;
    }
    public boolean requestMoney(Transfer transfer){
        boolean isRequested = false;
        try {
            isRequested =  restTemplate.exchange(api_base_url + "/request", HttpMethod.POST,getTransferEntity(transfer),boolean.class).getBody();

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return isRequested;
    }

}
