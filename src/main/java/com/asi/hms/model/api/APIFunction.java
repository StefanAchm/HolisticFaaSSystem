package com.asi.hms.model.api;

import com.asi.hms.model.db.DBFunction;

import java.util.UUID;

public class APIFunction {

    private UUID id;

    private String filePath;

    private String name;

    public APIFunction() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static APIFunction fromDBFunction(DBFunction dbFunction) {

        APIFunction apiFunction = new APIFunction();

        apiFunction.setId(dbFunction.getId());
        apiFunction.setFilePath(dbFunction.getFilePath());
        apiFunction.setName(dbFunction.getName());

        return apiFunction;

    }

}
