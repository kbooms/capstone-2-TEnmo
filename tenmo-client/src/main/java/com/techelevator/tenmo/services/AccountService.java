package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AccountService {


    public  String api_base_url ;
    private RestTemplate restTemplate = new RestTemplate();
    private String token;
    public AccountService(String api_base_url, String token) {
        this.token = token;
        this.api_base_url =api_base_url + "/accounts" ;
    }
    private HttpEntity<Account> getAccountEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>( headers);
    }

   public Account[] getAllAccounts() {
       Account [] accounts = null;
       try {
          return  restTemplate.exchange(api_base_url, HttpMethod.GET, getAccountEntity(),Account[].class).getBody();

       } catch (RestClientResponseException e) {
           BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
       } catch (ResourceAccessException e) {
           BasicLogger.log(e.getMessage());
       }
       return accounts;
   }

    public Account getAccountMatchingUsername(long userId) {
        Account account = new Account();
        try {
            account = restTemplate.exchange(api_base_url+"/" +  userId, HttpMethod.GET, getAccountEntity(),Account.class).getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }


}