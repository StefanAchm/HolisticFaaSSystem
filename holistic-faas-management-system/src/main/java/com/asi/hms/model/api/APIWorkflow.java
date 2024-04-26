package com.asi.hms.model.api;

import com.asi.hms.model.db.DBWorkflow;

import java.util.List;
import java.util.UUID;

public class APIWorkflow {

    private UUID id;

    private String name;

    private String description;

    private List<APIFunction2> functions;

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

    public List<APIFunction2> getFunctions() {
        return functions;
    }

    public void setFunctions(List<APIFunction2> functions) {
        this.functions = functions;
    }

    public static APIWorkflow fromDBWorkflow(DBWorkflow dbWorkflow) {
        APIWorkflow apiWorkflow = new APIWorkflow();
        apiWorkflow.setId(dbWorkflow.getId());
        apiWorkflow.setName(dbWorkflow.getName());
        apiWorkflow.setDescription(dbWorkflow.getDescription());
        apiWorkflow.setFunctions(dbWorkflow.getFunctions().stream().map(APIFunction2::fromDBFunction).toList());
        return apiWorkflow;
    }
}
