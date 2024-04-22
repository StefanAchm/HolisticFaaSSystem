package com.asi.hms.model.db;

import com.asi.hms.enums.DeployStatus;
import com.asi.hms.model.api.APIFunctionDeployment;

import javax.persistence.*;
import java.net.URI;
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
    @JoinColumn(name = "function_implementation_id")
    private DBFunctionImplementation functionImplementation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private DBUser user;

    @ManyToOne
    @JoinColumn(name = "function_id")
    private DBFunction function;

    @ManyToOne
    @JoinColumn(name = "workflow_deployment_id")
    private DBWorkflowDeployment workflowDeployment;

    public DBFunctionDeployment() {
        this.status = DeployStatus.CREATED;
        this.statusMessage = "Deployment not started";
    }

    public static DBFunctionDeployment fromAPIFunctionDeployment(
            APIFunctionDeployment apiFunctionDeployment,
            DBUser dbUser,
            DBFunctionImplementation dbFunctionImplementation

    ) {

        DBFunctionDeployment dbFunctionDeployment = new DBFunctionDeployment();

        dbFunctionDeployment.setProvider(apiFunctionDeployment.getProvider().toString());
        dbFunctionDeployment.setMemory(apiFunctionDeployment.getMemory());
        dbFunctionDeployment.setTimeoutSecs(apiFunctionDeployment.getTimeoutSecs());
        dbFunctionDeployment.setHandler(apiFunctionDeployment.getHandler());
        dbFunctionDeployment.setRegion(apiFunctionDeployment.getRegion());
        dbFunctionDeployment.setRuntime(apiFunctionDeployment.getRuntime());

        dbFunctionDeployment.setUser(dbUser);
        dbFunctionDeployment.setFunctionImplementation(dbFunctionImplementation);

        return dbFunctionDeployment;


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

    public DBFunctionImplementation getFunctionImplementation() {
        return functionImplementation;
    }

    public void setFunctionImplementation(DBFunctionImplementation function) {
        this.functionImplementation = function;
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

    public void setStatusWithMessage(DeployStatus status, String message) {
        this.status = status;
        this.statusMessage = message;
    }

    public String getUniqueName() {
        // TODO: setting a unique name for now, not sure if that is wanted!
        return this.getFunctionImplementation().getFunctionType().getName() + "-" + this.id;
    }

    // Todo: Use this functionname instead of uniqueName!
    public DBFunction getFunction() {
        return function;
    }

    public void setFunction(DBFunction function) {
        this.function = function;
    }

    public DBWorkflowDeployment getWorkflowDeployment() {
        return workflowDeployment;
    }

    public void setWorkflowDeployment(DBWorkflowDeployment workflowDeployment) {
        this.workflowDeployment = workflowDeployment;
    }
}
