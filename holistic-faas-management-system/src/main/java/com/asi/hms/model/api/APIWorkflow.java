package com.asi.hms.model.api;

import com.asi.hms.model.db.DBWorkflow;

import java.util.List;
import java.util.UUID;

public class APIWorkflow {

    private UUID id;

    private String name;

    private String description;

    private List<APIFunction> functions;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<APIFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<APIFunction> functions) {
        this.functions = functions;
    }

    public static APIWorkflow fromDBWorkflow(DBWorkflow dbWorkflow) {
        APIWorkflow apiWorkflow = new APIWorkflow();
        apiWorkflow.setId(dbWorkflow.getId());
        apiWorkflow.setName(dbWorkflow.getName());
        apiWorkflow.setDescription(dbWorkflow.getDescription());
        apiWorkflow.setFunctions(dbWorkflow.getFunctions().stream().map(APIFunction::fromDBFunction).toList());
        return apiWorkflow;
    }
}
