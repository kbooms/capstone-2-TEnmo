package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
//  Implemented a User Service, not sure we need this, but I thought it might help with the sendBucks method
public class UserService {

    private RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:8080/";

    public UserService() {
        this.restTemplate = new RestTemplate();
    }

    public User[] getAllUsers(AuthenticatedUser authenticatedUser) {
        User[] users = null;
        try {
            users = restTemplate.exchange(API_BASE_URL + "/users", HttpMethod.GET,
                    makeEntity(authenticatedUser), User[].class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println("Cannot resolve request. Error code: " + e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Unable to reach server. Please try again.");
        }
        return users;
    }

    public User getUserByUserId(AuthenticatedUser authenticatedUser, long id) {
        User user = null;
        try {
            user = restTemplate.exchange(API_BASE_URL + "/users/" + id, HttpMethod.GET,
                    makeEntity(authenticatedUser), User.class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println("Cannot resolve request. Error code: " + e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Unable to reach server. Please try again.");
        }
        return user;
    }

    private HttpEntity makeEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity(headers);
        return entity;
    }
}
