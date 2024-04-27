package com.asi.hms.model.api;

import com.asi.hms.enums.MigrationType;

import java.util.List;

public class APIMigrationPreparation {

    private MigrationType migrationType;
    private String target;

    private List<APIFunctionFlat> functions;

    public MigrationType getMigrationType() {
        return migrationType;
    }

    public void setMigrationType(MigrationType migrationType) {
        this.migrationType = migrationType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }


    public List<APIFunctionFlat> getFunctions() {
        return functions;
    }

    public void setFunctions(List<APIFunctionFlat> functions) {
        this.functions = functions;
    }
}
