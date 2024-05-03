package com.asi.hms.model.db;

import com.asi.hms.enums.DeployStatus;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workflow_deployments")
public class DBWorkflowDeployment {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DeployStatus status;

    @Column(columnDefinition = "TEXT")
    private String statusMessage;

    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private DBWorkflow workflow;

    @OneToMany(mappedBy = "workflowDeployment")
    private List<DBFunctionDeployment> functionDeployments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private DBUser user;

    public DBWorkflowDeployment() {
        this.status = DeployStatus.CREATED;
        this.statusMessage = "Deployment created";
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public DBUser getUser() {
        return user;
    }

    public void setUser(DBUser user) {
        this.user = user;
    }
}
