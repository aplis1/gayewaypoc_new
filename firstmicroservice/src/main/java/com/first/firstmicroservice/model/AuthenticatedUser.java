package com.first.firstmicroservice.model;

import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUser {

    private String username;
    private String tokenId;

    public AuthenticatedUser() {
    }

    public AuthenticatedUser(String username, String tokenId) {
        this.username = username;
        this.tokenId = tokenId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
