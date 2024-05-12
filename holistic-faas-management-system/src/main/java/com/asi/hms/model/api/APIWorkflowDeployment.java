package com.asi.hms.model.api;

import com.asi.hms.enums.DeployStatus;
import com.asi.hms.model.db.DBFunctionDeployment;
import com.asi.hms.model.db.DBWorkflowDeployment;
import com.asi.hms.utils.WorkflowDeploymentDetailsGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class APIWorkflowDeployment {

    private UUID id;
    private String name;

    private String deploymentDetails;

    private APIWorkflow workflow;

    private APIUser user;

    private List<APIFunctionFlat> functionDefinitions;

    private DeployStatus status;

    public APIWorkflowDeployment() {
        this.functionDefinitions = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeploymentDetails() {
        return deploymentDetails;
    }

    public void setDeploymentDetails(String deploymentDetails) {
        this.deploymentDetails = deploymentDetails;
    }

    public APIWorkflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(APIWorkflow workflow) {
        this.workflow = workflow;
    }

    public List<APIFunctionFlat> getFunctionDefinitions() {
        return functionDefinitions;
    }

    public void setFunctionDefinitions(List<APIFunctionFlat> functionDefinitions) {
        this.functionDefinitions = functionDefinitions;
    }

    public APIUser getUser() {
        return user;
    }

    public void setUser(APIUser user) {
        this.user = user;
    }

    public DeployStatus getStatus() {
        return status;
    }

    public void setStatus(DeployStatus status) {
        this.status = status;
    }

    public static APIWorkflowDeployment fromDBWorkflowDeployment(DBWorkflowDeployment dbWorkflowDeployment) {

        APIWorkflowDeployment apiWorkflowDeployment = new APIWorkflowDeployment();

        apiWorkflowDeployment.setId(dbWorkflowDeployment.getId());
        apiWorkflowDeployment.setName(dbWorkflowDeployment.getName());
        apiWorkflowDeployment.setWorkflow(APIWorkflow.fromDBWorkflow(dbWorkflowDeployment.getWorkflow()));
        apiWorkflowDeployment.setUser(APIUser.fromDBUser(dbWorkflowDeployment.getUser()));
        apiWorkflowDeployment.setFunctionDefinitions(new ArrayList<>());

        apiWorkflowDeployment.setStatus(DeployStatus.CREATED);

        if(dbWorkflowDeployment.getFunctionDeployments() != null) {

            for (DBFunctionDeployment dbFunctionDeployment : dbWorkflowDeployment.getFunctionDeployments()) {

                apiWorkflowDeployment.getFunctionDefinitions().add(APIFunctionFlat.fromDBFunction(dbFunctionDeployment));

            }

            // If any of the functionDeployments has an error, set the error status
            if (dbWorkflowDeployment.getFunctionDeployments().stream().anyMatch(fd -> fd.getStatus() == DeployStatus.FAILED)) {
                apiWorkflowDeployment.setStatus(DeployStatus.FAILED);
            }

            if(dbWorkflowDeployment.getFunctionDeployments().stream().anyMatch(fd -> fd.getStatus() == DeployStatus.CHANGED)) {
                apiWorkflowDeployment.setStatus(DeployStatus.CHANGED);
            }

            if(dbWorkflowDeployment.getFunctionDeployments().stream().anyMatch(fd -> fd.getStatus() == DeployStatus.STARTED)) {
                apiWorkflowDeployment.setStatus(DeployStatus.STARTED);
            }

            if(dbWorkflowDeployment.getFunctionDeployments().stream().allMatch(fd -> fd.getStatus() == DeployStatus.DEPLOYED)) {
                apiWorkflowDeployment.setStatus(DeployStatus.DEPLOYED);
            }

        }

        apiWorkflowDeployment.setDeploymentDetails(WorkflowDeploymentDetailsGenerator.getDeploymentDetails(apiWorkflowDeployment));

        return apiWorkflowDeployment;

    }

}
