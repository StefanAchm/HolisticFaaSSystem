package com.asi.hms.model.api;

import com.asi.hms.enums.MigrationType;

public class APIWorkflowDeploymentMigration {

    private MigrationType migrationType;
    private String target;

    private APIWorkflowDeployment workflowDeployment;

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

    public APIWorkflowDeployment getWorkflowDeployment() {
        return workflowDeployment;
    }

    public void setWorkflowDeployment(APIWorkflowDeployment workflowDeployment) {
        this.workflowDeployment = workflowDeployment;
    }
}
