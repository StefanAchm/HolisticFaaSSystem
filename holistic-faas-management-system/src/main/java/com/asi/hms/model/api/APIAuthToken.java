package com.asi.hms.model.api;

public class APIAuthToken {

    private String token;

    public APIAuthToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
