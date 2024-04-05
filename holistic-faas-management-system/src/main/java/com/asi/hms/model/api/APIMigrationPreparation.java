package com.asi.hms.model.api;

import com.asi.hms.utils.cloudproviderutils.enums.MigrationType;

import java.util.List;

public class APIMigrationPreparation {

    private MigrationType migrationType;
    private String target;

    private List<APIFunction> functions;

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


    public List<APIFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<APIFunction> functions) {
        this.functions = functions;
    }
}
