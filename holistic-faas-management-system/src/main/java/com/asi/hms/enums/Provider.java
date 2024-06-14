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

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
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

    public UserInterface getUserFromInputStream(InputStream inputStream) {
        return switch (this) {
            case AWS -> UserAWS.fromInputStream(inputStream);
            case GCP -> UserGCP.fromInputStream(inputStream);
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

    // https://console.aws.amazon.com/lambda/home?region=eu-west-2#/functions/function1-063a989a-bcc0-480c-8554-69d5aa5025b9
    // https://console.cloud.google.com/functions/details/europe-west2/function2?project=holistic-faas
    public URI getUrlFromFunctionDeployment(String region, String uniqueName) throws URISyntaxException {
        return switch (this) {
            case AWS -> new URI("https://console.aws.amazon.com/lambda/home?region=" + region + "#/functions/" + uniqueName);
            case GCP -> new URI("https://console.cloud.google.com/functions/details/" + region + "/" + uniqueName);
        };
    }
}
