package com.asi.hms.model.api;

import com.asi.hms.model.db.DBUser;

import java.util.List;
import java.util.UUID;

public class APIUser {

    private UUID id;

    private String username;
    private String password;

    private List<APIUserCredentials> userCredentials;

    public static APIUser fromDBUser(DBUser dbUser) {
        APIUser apiUser = new APIUser();
        apiUser.setId(dbUser.getId());
        apiUser.setUsername(dbUser.getUsername());
        return apiUser;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<APIUserCredentials> getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(List<APIUserCredentials> userCredentials) {
        this.userCredentials = userCredentials;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
