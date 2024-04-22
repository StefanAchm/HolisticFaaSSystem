package com.asi.hms.model.db;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "functions")
public class DBWorkflowFunction {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private DBWorkflow workflow;

    public DBWorkflowFunction() {
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

    public DBWorkflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(DBWorkflow workflow) {
        this.workflow = workflow;
    }
}
