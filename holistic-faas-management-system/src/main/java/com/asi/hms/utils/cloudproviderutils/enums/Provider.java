package com.asi.hms.utils.cloudproviderutils.enums;

import com.asi.hms.utils.cloudproviderutils.deploy.DeployAWS;
import com.asi.hms.utils.cloudproviderutils.deploy.DeployGCP;
import com.asi.hms.utils.cloudproviderutils.deploy.DeployInterface;
import com.asi.hms.utils.cloudproviderutils.model.UserAWS;
import com.asi.hms.utils.cloudproviderutils.model.UserGCP;
import com.asi.hms.utils.cloudproviderutils.model.UserInterface;

import java.nio.file.Path;

public enum Provider {

    AWS,

    GCP;

    public DeployInterface getDeployer() {
        return switch (this) {
            case AWS -> new DeployAWS();
            case GCP -> new DeployGCP();
        };
    }

    public UserInterface getUserFromFile(Path filePath) {
        return switch (this) {
            case AWS -> UserAWS.fromFile(filePath);
            case GCP -> UserGCP.fromFile(filePath);
        };
    }

    public RegionInterface getRegionFromCode(String region) {
        return switch (this) {
            case AWS -> RegionAWS.fromCode(region);
            case GCP -> RegionGCP.fromCode(region);
        };
    }

    public RuntimeInterface getRuntimeFromCode(String runtime) {
        return switch (this) {
            case AWS -> RuntimeAWS.fromCode(runtime);
            case GCP -> RuntimeGCP.fromCode(runtime);
        };
    }

    public Class<? extends RegionInterface> getRegionClass() {
        return switch (this) {
            case AWS -> RegionAWS.class;
            case GCP -> RegionGCP.class;
        };
    }

    public Class<? extends RuntimeInterface> getRuntimeClass() {
        return switch (this) {
            case AWS -> RuntimeAWS.class;
            case GCP -> RuntimeGCP.class;
        };
    }

}
