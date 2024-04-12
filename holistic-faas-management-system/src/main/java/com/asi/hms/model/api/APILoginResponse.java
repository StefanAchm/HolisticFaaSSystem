package com.asi.hms.model.api;

public class APILoginResponse {

    private String token;

    private APIUser user;

    public APILoginResponse(APIUser apiUser, String token) {
        this.user = apiUser;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public APIUser getUser() {
        return user;
    }

    public void setUser(APIUser user) {
        this.user = user;
    }
}
