package com.asi.hms.model.db;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "workflow_deployments")
public class DBWorkflowDeployment {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private DBWorkflow workflow;

    @ManyToOne
    @JoinColumn(name = "function_deployment_id")
    private DBFunctionDeployment functionDeployment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private DBUser user; // TODO: Problem with user in functionDeployment

    public DBWorkflowDeployment() {
    }

    public UUID getId() {
        return id;
    }

    public DBWorkflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(DBWorkflow workflow) {
        this.workflow = workflow;
    }

    public DBFunctionDeployment getFunctionDeployment() {
        return functionDeployment;
    }

    public void setFunctionDeployment(DBFunctionDeployment functionDeployment) {
        this.functionDeployment = functionDeployment;
    }

    public DBUser getUser() {
        return user;
    }

    public void setUser(DBUser user) {
        this.user = user;
    }
}
