package com.asi.hms.model.api;

import com.asi.hms.enums.DeployStatus;
import com.asi.hms.utils.cloudproviderutils.enums.*;
import com.asi.hms.model.db.DBFunctionDeployment;
import com.fasterxml.jackson.annotation.JsonGetter;

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
    private String statusMessage;

    private UUID userId;
    private String userName;

    private UUID functionImplementationId;

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
        apiFunctionDeployment.setStatusMessage(dbFunctionDeployment.getStatusMessage());

        apiFunctionDeployment.setUserId(dbFunctionDeployment.getUser().getId());
        apiFunctionDeployment.setUserName(dbFunctionDeployment.getUser().getUsername());
        apiFunctionDeployment.setFunctionImplementationId(dbFunctionDeployment.getFunctionImplementation().getId());

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

    @JsonGetter("handlerShort")
    public String getHandlerShort() {

        // E.g. if a handler is "com.asi.hsg.HelloWorldHandler::handleRequest"
        // then the short handler is "c.a.h.HelloWorldHandler::handleRequest"

        String[] parts = handler.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            sb.append(parts[i].charAt(0)).append(".");
        }

        sb.append(parts[parts.length - 1]);

        return sb.toString();

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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getFunctionImplementationId() {
        return functionImplementationId;
    }

    public void setFunctionImplementationId(UUID functionImplementationId) {
        this.functionImplementationId = functionImplementationId;
    }

    public DeployStatus getStatus() {
        return status;
    }

    public void setStatus(DeployStatus status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
