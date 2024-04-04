package com.asi.hms.model.api;

import com.asi.hms.model.db.DBFunctionImplementation;
import com.asi.hms.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class APIFunctionImplementation {

    private UUID id;

    private String filePath;

    private String fileName;

    private UUID functionTypeId;

    private List<APIFunctionDeployment> functionDeployments;

    public APIFunctionImplementation() {
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

        APIFunctionImplementation apiFunctionImplementation = new APIFunctionImplementation();

        apiFunctionImplementation.setId(dbFunctionImplementation.getId());
        apiFunctionImplementation.setFilePath(dbFunctionImplementation.getFilePath());
        apiFunctionImplementation.setFileName(FileUtil.getFileNameFromPath(dbFunctionImplementation.getFilePath()));

        apiFunctionImplementation.setFunctionTypeId(dbFunctionImplementation.getFunctionType().getId());

        apiFunctionImplementation.setFunctionDeployments(new ArrayList<>());

        // TODO
        dbFunctionImplementation.getFunctionDeployments().stream()
                .map(APIFunctionDeployment::fromDBFunctionDeployment)
                .forEach(apiFunctionImplementation.getFunctionDeployments()::add);

        return apiFunctionImplementation;

    }

}
