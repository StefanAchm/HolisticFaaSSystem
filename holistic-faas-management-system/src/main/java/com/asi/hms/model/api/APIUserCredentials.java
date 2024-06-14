package com.asi.hms.model.api;

import com.asi.hms.model.db.DBUserCredentials;
import com.asi.hms.utils.FileUtil;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.UUID;

public class APIUserCredentials {

    private APIUser user;

    private String provider;

    private UUID userId;

    public static APIUserCredentials fromDBUserCredentials(DBUserCredentials dbUserCredentials) {

        APIUserCredentials apiUserCredentials = new APIUserCredentials();
        apiUserCredentials.setUser(APIUser.fromDBUser(dbUserCredentials.getUser()));
        apiUserCredentials.setProvider(dbUserCredentials.getProvider());
        apiUserCredentials.setUserId(dbUserCredentials.getUser().getId());

        return apiUserCredentials;

    }

    public APIUser getUser() {
        return user;
    }

    public void setUser(APIUser user) {
        this.user = user;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

}
