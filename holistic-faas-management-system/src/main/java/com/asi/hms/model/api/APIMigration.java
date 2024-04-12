package com.asi.hms.model.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class APIMigration {

    private Set<APIMigrationObject> userMigrations;
    private Set<APIMigrationObject> regionMigrations;
    private Set<APIMigrationObject> runtimeMigrations;

    private List<APIFunction> functions;

    public APIMigration() {

        this.userMigrations = new HashSet<>();
        this.regionMigrations = new HashSet<>();
        this.runtimeMigrations = new HashSet<>();

        this.functions = new ArrayList<>();

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

    public List<APIFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<APIFunction> functions) {
        this.functions = functions;
    }
}
