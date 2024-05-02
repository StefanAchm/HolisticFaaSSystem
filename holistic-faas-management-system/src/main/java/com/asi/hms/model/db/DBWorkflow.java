package com.asi.hms.model.db;

import com.asi.hms.model.api.APIWorkflow;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workflows")
public class DBWorkflow extends Auditable {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "workflow")
    private List<DBFunction> functions;

    @OneToMany(mappedBy = "workflow")
    private List<DBWorkflowDeployment> workflowDeployments;

    public DBWorkflow() {
    }

    public static DBWorkflow fromAPIWorkflow(APIWorkflow workflow1) {
        DBWorkflow dbWorkflow = new DBWorkflow();
        dbWorkflow.setId(workflow1.getId());
        dbWorkflow.setName(workflow1.getName());
        dbWorkflow.setDescription(workflow1.getDescription());
        return dbWorkflow;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<DBFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<DBFunction> functions) {
        this.functions = functions;
    }

    public List<DBWorkflowDeployment> getWorkflowDeployments() {
        return workflowDeployments;
    }

    public void setWorkflowDeployments(List<DBWorkflowDeployment> workflowDeployments) {
        this.workflowDeployments = workflowDeployments;
    }
}
