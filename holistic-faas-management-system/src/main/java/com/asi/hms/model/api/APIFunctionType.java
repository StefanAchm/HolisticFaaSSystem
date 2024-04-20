package com.asi.hms.model.api;

import com.asi.hms.model.db.DBFunctionType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class APIFunctionType {

    private UUID id;

    private String name;

    private List<APIFunctionImplementation> functionImplementations;

    public APIFunctionType() {
        this.functionImplementations = new ArrayList<>();
    }

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

    public List<APIFunctionImplementation> getFunctionImplementations() {
        return functionImplementations;
    }

    public void setFunctionImplementations(List<APIFunctionImplementation> functionImplementations) {
        this.functionImplementations = functionImplementations;
    }

    public static APIFunctionType fromDBFunctionType(DBFunctionType dbFunctionType) {

        APIFunctionType apiFunctionType = new APIFunctionType();

        apiFunctionType.setId(dbFunctionType.getId());
        apiFunctionType.setName(dbFunctionType.getName());

        if(dbFunctionType.getFunctionImplementations() != null) {

            dbFunctionType.getFunctionImplementations()
                    .stream()
                    .map(APIFunctionImplementation::fromDBFunctionImplementation)
                    .forEach(apiFunctionType.getFunctionImplementations()::add);

        }

        return apiFunctionType;

    }

}
