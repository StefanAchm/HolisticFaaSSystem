package com.asi.hms.model.api;

import com.asi.hms.model.db.DBFunctionImplementation;
import com.asi.hms.utils.FileUtil;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class APIFunctionImplementation {

    private UUID id;

    private String filePath;

    private String name;

    private UUID functionTypeId;

    private List<APIFunctionDeployment> functionDeployments;

    public APIFunctionImplementation() {
        this.functionDeployments = new ArrayList<>();
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

    @JsonGetter("fileName")
    public String getFileName() {
        return FileUtil.getFileNameFromPath(this.filePath);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getFunctionTypeId() {
        return functionTypeId;
    }

    public void setFunctionTypeId(UUID functionTypeId) {
        this.functionTypeId = functionTypeId;
    }

    public List<APIFunctionDeployment> getFunctionDeployments() {
        return functionDeployments;
    }

    public void setFunctionDeployments(List<APIFunctionDeployment> functionDeployments) {
        this.functionDeployments = functionDeployments;
    }

    public static APIFunctionImplementation fromDBFunctionImplementation(DBFunctionImplementation dbFunctionImplementation) {
        // Adding deployments results in extremely large responses!
        return fromDBFunctionImplementation(dbFunctionImplementation, false);
    }

    public static APIFunctionImplementation fromDBFunctionImplementation(DBFunctionImplementation dbFunctionImplementation, boolean withDeployments) {

        APIFunctionImplementation apiFunctionImplementation = new APIFunctionImplementation();

        apiFunctionImplementation.setId(dbFunctionImplementation.getId());
        apiFunctionImplementation.setFilePath(dbFunctionImplementation.getFilePath());
        apiFunctionImplementation.setName(dbFunctionImplementation.getName());

        apiFunctionImplementation.setFunctionTypeId(dbFunctionImplementation.getFunctionType().getId());

        if(dbFunctionImplementation.getFunctionDeployments() != null && withDeployments) {

            dbFunctionImplementation.getFunctionDeployments()
                    .stream()
                    .map(APIFunctionDeployment::fromDBFunctionDeployment)
                    .forEach(apiFunctionImplementation.getFunctionDeployments()::add);

        }

        return apiFunctionImplementation;

    }

}
