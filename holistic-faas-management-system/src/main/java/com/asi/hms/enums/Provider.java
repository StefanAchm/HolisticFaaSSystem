package com.asi.hms.enums;

import com.asi.hms.model.db.DBFunctionDeployment;
import com.asi.hms.service.WebSocketSessionService;
import com.asi.hms.utils.ProgressHandler;
import com.asi.hms.utils.cloudproviderutils.deploy.DeployAWS;
import com.asi.hms.utils.cloudproviderutils.deploy.DeployGCP;
import com.asi.hms.utils.cloudproviderutils.deploy.DeployerInterface;
import com.asi.hms.model.UserAWS;
import com.asi.hms.model.UserGCP;
import com.asi.hms.model.UserInterface;

import java.nio.file.Path;

public enum Provider {

    AWS,

    GCP;


    public DeployerInterface getDeployer(UserInterface user) {
        return switch (this) {
            case AWS -> new DeployAWS((UserAWS) user);
            case GCP -> new DeployGCP((UserGCP) user);
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

    public ProgressHandler getProgressHandler(DBFunctionDeployment dbFunctionDeployment, WebSocketSessionService sessionService) {
        return switch (this) {
            case AWS -> new ProgressHandler(dbFunctionDeployment, DeployAWS.STEPS, sessionService);
            case GCP -> new ProgressHandler(dbFunctionDeployment, DeployGCP.STEPS, sessionService);
        };
    }
}
