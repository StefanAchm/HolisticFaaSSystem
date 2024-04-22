package com.asi.hms.model.db;

import javax.persistence.*;
import java.util.List;
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

    @OneToMany(mappedBy = "workflowDeployment")
    private List<DBFunctionDeployment> functionDeployments;

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

    public List<DBFunctionDeployment> getFunctionDeployments() {
        return functionDeployments;
    }

    public void setFunctionDeployments(List<DBFunctionDeployment> functionDeployments) {
        this.functionDeployments = functionDeployments;
    }
}
