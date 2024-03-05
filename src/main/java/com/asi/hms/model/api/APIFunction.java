package com.asi.hms.model.api;

import com.asi.hms.enums.Provider;
import com.asi.hms.model.db.DBFunction;

import java.util.UUID;

public class APIFunction {

    private UUID id;

    private Provider provider;

    private String filePath;
    private String name;
    private Integer memory;
    private Integer timeoutSecs;
    private String handler;
    private String region;
    private String runtime;

    // TODO: Currently just a path to the cloud provider's user file in the resources (e.g. AWS: auth/stefan01/aws.properties)
    private String credentialsPath;

    private String userName;

    public APIFunction() {
    }

    public static APIFunction fromDBFunction(DBFunction dbFunction) {

        APIFunction apiFunction = new APIFunction();

        apiFunction.setId(dbFunction.getId());
        apiFunction.setProvider(Provider.valueOf(dbFunction.getProvider()));
        apiFunction.setFilePath(dbFunction.getFilePath());
        apiFunction.setName(dbFunction.getName());
        apiFunction.setMemory(dbFunction.getMemory());
        apiFunction.setTimeoutSecs(dbFunction.getTimeoutSecs());
        apiFunction.setHandler(dbFunction.getHandler());
        apiFunction.setRegion(dbFunction.getRegion());
        apiFunction.setRuntime(dbFunction.getRuntime());
        apiFunction.setUserName(dbFunction.getUser().getUsername());

        return apiFunction;

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getTimeoutSecs() {
        return timeoutSecs;
    }

    public void setTimeoutSecs(Integer timeoutSecs) {
        this.timeoutSecs = timeoutSecs;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getCredentialsPath() {
        return credentialsPath;
    }

    public void setCredentialsPath(String credentialsPath) {
        this.credentialsPath = credentialsPath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
