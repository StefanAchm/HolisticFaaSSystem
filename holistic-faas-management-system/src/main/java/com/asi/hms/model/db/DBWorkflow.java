package com.asi.hms.model.db;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workflows")
public class DBWorkflow {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String description;

    @OneToMany
    private List<DBWorkflowFunction> functions;

    @OneToMany
    private List<DBFunctionType> functionTypes;

    public DBWorkflow() {
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

    public List<DBWorkflowFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<DBWorkflowFunction> functions) {
        this.functions = functions;
    }

    public List<DBFunctionType> getFunctionTypes() {
        return functionTypes;
    }

    public void setFunctionTypes(List<DBFunctionType> functionTypes) {
        this.functionTypes = functionTypes;
    }
}
