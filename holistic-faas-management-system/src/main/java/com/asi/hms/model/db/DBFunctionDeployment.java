package com.asi.hms.model.db;

import com.asi.hms.enums.DeployStatus;

import javax.persistence.*;
import java.util.UUID;

/**
 * TODO: Add time of deployment
 */
@Entity
@Table(name = "function_deployments")
public class DBFunctionDeployment implements ProgressObjectInterface {

    @Id
    @GeneratedValue
    private UUID id;

    private String provider;

    private Integer memory;
    private Integer timeoutSecs;
    private String handler;
    private String region;
    private String runtime;

    @Enumerated(EnumType.STRING)
    private DeployStatus status;

    @Column(columnDefinition = "TEXT")
    private String statusMessage;

    @ManyToOne
    @JoinColumn(name = "function_id")
    private DBFunction function;

    @ManyToOne
    @JoinColumn(name = "user_username")
    private DBUser user;

    public DBFunctionDeployment() {
        this.status = DeployStatus.CREATED;
    }

    public UUID getId() {
        return id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
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

    public DBFunction getFunction() {
        return function;
    }

    public void setFunction(DBFunction function) {
        this.function = function;
    }

    public DBUser getUser() {
        return user;
    }

    public void setUser(DBUser user) {
        this.user = user;
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
