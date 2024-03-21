package com.asi.hms.model.api;

import com.asi.hms.model.db.DBUser;

public class APIUser {

    private String username;

    private String credentialsFilePath;

    private String provider;

    public static APIUser fromDBUser(DBUser dbUser) {

        APIUser apiUser = new APIUser();
        apiUser.setUsername(dbUser.getUsername());
        apiUser.setCredentialsFilePath(dbUser.getCredentialsFilePath());
        apiUser.setProvider(dbUser.getProvider());

        return apiUser;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCredentialsFilePath() {
        return credentialsFilePath;
    }

    public void setCredentialsFilePath(String credentialsFilePath) {
        this.credentialsFilePath = credentialsFilePath;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

}
