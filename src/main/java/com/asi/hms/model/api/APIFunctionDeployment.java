package com.asi.hms.model.api;

import com.asi.hms.enums.DeployStatus;
import com.asi.hms.enums.Provider;
import com.asi.hms.model.db.DBFunctionDeployment;

import java.util.UUID;

public class APIFunctionDeployment {

    private UUID id;

    private Provider provider;

    private Integer memory;
    private Integer timeoutSecs;
    private String handler;
    private String region;
    private String runtime;
    private DeployStatus status;

    private String userName;
    private UUID functionId;

    public static APIFunctionDeployment fromDBFunctionDeployment(DBFunctionDeployment dbFunctionDeployment) {

        APIFunctionDeployment apiFunctionDeployment = new APIFunctionDeployment();

        apiFunctionDeployment.setId(dbFunctionDeployment.getId());
        apiFunctionDeployment.setProvider(Provider.valueOf(dbFunctionDeployment.getProvider()));
        apiFunctionDeployment.setMemory(dbFunctionDeployment.getMemory());
        apiFunctionDeployment.setTimeoutSecs(dbFunctionDeployment.getTimeoutSecs());
        apiFunctionDeployment.setHandler(dbFunctionDeployment.getHandler());
        apiFunctionDeployment.setRegion(dbFunctionDeployment.getRegion());
        apiFunctionDeployment.setRuntime(dbFunctionDeployment.getRuntime());
        apiFunctionDeployment.setStatus(dbFunctionDeployment.getStatus());

        apiFunctionDeployment.setUserName(dbFunctionDeployment.getUser().getUsername());
        apiFunctionDeployment.setFunctionId(dbFunctionDeployment.getFunction().getId());

        return apiFunctionDeployment;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getFunctionId() {
        return functionId;
    }

    public void setFunctionId(UUID functionId) {
        this.functionId = functionId;
    }

    public DeployStatus getStatus() {
        return status;
    }

    public void setStatus(DeployStatus status) {
        this.status = status;
    }
}
