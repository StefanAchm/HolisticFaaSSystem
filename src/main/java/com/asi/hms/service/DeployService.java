package com.asi.hms.service;

import com.asi.hms.enums.*;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.*;
import com.asi.hms.model.api.APIFunction;
import com.asi.hms.utils.FileUtil;
import com.asi.hms.utils.cloudproviderutils.DeployAWS;
import com.asi.hms.utils.cloudproviderutils.DeployGCP;
import com.asi.hms.utils.cloudproviderutils.DeployInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeployService {

    private static final Logger logger = LoggerFactory.getLogger(DeployService.class);

    public boolean deploy(APIFunction APIFunction) {

        DeployInterface deployInterface;
        UserInterface user;
        RegionInterface regionInterface;
        RuntimeInterface runtimeInterface;

        logger.info("Deploying function {} to provider {} at region {} for user {}",
                APIFunction.getName(),
                APIFunction.getProvider(),
                APIFunction.getRegion(),
                APIFunction.getUser());

        switch (APIFunction.getProvider()) {
            case AWS -> {
                deployInterface = new DeployAWS();
                user = UserAWS.fromResources(APIFunction.getUser());
                regionInterface = RegionAWS.valueOf(APIFunction.getRegion());
                runtimeInterface = RuntimeAWS.valueOf(APIFunction.getRuntime());
            }
            case GCP -> {
                deployInterface = new DeployGCP();
                user = UserGCP.fromResources(APIFunction.getUser());
                regionInterface = RegionGCP.valueOf(APIFunction.getRegion());
                runtimeInterface = RuntimeGCP.valueOf(APIFunction.getRuntime());
            } default -> throw new HolisticFaaSException("Provider not supported");
        }

        Function function = new Function();

        // TODO: support for other file paths (currently only resources)
        function.setFilePath(FileUtil.getFilePathFromResourcesFile(APIFunction.getFilePath()));

        function.setName(APIFunction.getName());
        function.setMemory(APIFunction.getMemory());
        function.setTimeoutSecs(APIFunction.getTimeoutSecs());
        function.setHandler(APIFunction.getHandler());
        function.setRegionInterface(regionInterface);
        function.setRuntimeInterface(runtimeInterface);

        return deployInterface.deployFunction(function, user);

    }

}
