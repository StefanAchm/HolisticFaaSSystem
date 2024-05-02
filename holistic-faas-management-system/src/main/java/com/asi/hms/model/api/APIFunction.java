package com.asi.hms.model.api;

import com.asi.hms.model.db.DBFunction;

import java.util.UUID;

public class APIFunction {

    private UUID id;

    private String name;

    private APIFunctionType functionType;

    private UUID workflowId;

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

    public APIFunctionType getFunctionType() {
        return functionType;
    }

    public void setFunctionType(APIFunctionType functionType) {
        this.functionType = functionType;
    }

    public UUID getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(UUID workflowId) {
        this.workflowId = workflowId;
    }

    public static APIFunction fromDBFunction(DBFunction dbFunction) {
        APIFunction apiFunction = new APIFunction();
        apiFunction.setId(dbFunction.getId());
        apiFunction.setName(dbFunction.getName());
        apiFunction.setFunctionType(APIFunctionType.fromDBFunctionType(dbFunction.getFunctionType()));
        return apiFunction;
    }
}
