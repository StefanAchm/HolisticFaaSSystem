package com.asi.hms.model.api;

import com.asi.hms.model.db.DBFunctionDeployment;
import com.asi.hms.model.db.DBWorkflowDeployment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class APIWorkflowDeployment {

    private UUID id;
    private String name;
    private APIWorkflow workflow;
    private APIUser user;
    private List<APIFunctionDeployment> functionDeployments;


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

    public APIWorkflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(APIWorkflow workflow) {
        this.workflow = workflow;
    }

    public List<APIFunctionDeployment> getFunctionDeployments() {
        return functionDeployments;
    }

    public void setFunctionDeployments(List<APIFunctionDeployment> functionDeployments) {
        this.functionDeployments = functionDeployments;
    }

    public APIUser getUser() {
        return user;
    }

    public void setUser(APIUser user) {
        this.user = user;
    }

    public static APIWorkflowDeployment fromDBWorkflowDeployment(DBWorkflowDeployment dbWorkflowDeployment) {

        APIWorkflowDeployment apiWorkflowDeployment = new APIWorkflowDeployment();

        apiWorkflowDeployment.setId(dbWorkflowDeployment.getId());
        apiWorkflowDeployment.setName(dbWorkflowDeployment.getName());
        apiWorkflowDeployment.setWorkflow(APIWorkflow.fromDBWorkflow(dbWorkflowDeployment.getWorkflow()));
        apiWorkflowDeployment.setUser(APIUser.fromDBUser(dbWorkflowDeployment.getUser()));
        apiWorkflowDeployment.setFunctionDeployments(new ArrayList<>());

        if(dbWorkflowDeployment.getFunctionDeployments() != null) {
            for (DBFunctionDeployment dbFunctionDeployment : dbWorkflowDeployment.getFunctionDeployments()) {
                apiWorkflowDeployment.getFunctionDeployments().add(APIFunctionDeployment.fromDBFunctionDeployment(dbFunctionDeployment));
            }
        }


        return apiWorkflowDeployment;

    }

}
