package com.asi.hms.model.api;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.Set;

public class APIWorkflowDeploymentMigrationResponse {

    // These 3 fields just serve as information in the response!
    private Set<APIMigrationObject> userMigrations;
    private Set<APIMigrationObject> regionMigrations;
    private Set<APIMigrationObject> runtimeMigrations;

    private APIWorkflowDeployment workflowDeployment;


    public APIWorkflowDeploymentMigrationResponse(APIWorkflowDeployment apiWorkflowDeployment) {
        this.workflowDeployment = apiWorkflowDeployment;
    }

    public Set<APIMigrationObject> getUserMigrations() {
        return userMigrations;
    }

    public void setUserMigrations(Set<APIMigrationObject> userMigrations) {
        this.userMigrations = userMigrations;
    }

    public Set<APIMigrationObject> getRegionMigrations() {
        return regionMigrations;
    }

    public void setRegionMigrations(Set<APIMigrationObject> regionMigrations) {
        this.regionMigrations = regionMigrations;
    }

    public Set<APIMigrationObject> getRuntimeMigrations() {
        return runtimeMigrations;
    }

    public void setRuntimeMigrations(Set<APIMigrationObject> runtimeMigrations) {
        this.runtimeMigrations = runtimeMigrations;
    }

    public APIWorkflowDeployment getWorkflowDeployment() {
        return workflowDeployment;
    }

    public void setWorkflowDeployment(APIWorkflowDeployment workflowDeployment) {
        this.workflowDeployment = workflowDeployment;
    }

    @JsonGetter("valid")
    public boolean isValid() {

        // If all migrationobjects have a source and target, the migration is valid

        return this.userMigrations.stream().allMatch(APIMigrationObject::isValid) &&
                this.regionMigrations.stream().allMatch(APIMigrationObject::isValid) &&
                this.runtimeMigrations.stream().allMatch(APIMigrationObject::isValid);

    }

}
