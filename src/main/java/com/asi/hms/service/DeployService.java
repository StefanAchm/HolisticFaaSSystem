package com.asi.hms.service;

import com.asi.hms.enums.*;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.*;
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

    public boolean deploy(DeployFunction deployFunction) {

        DeployInterface deployInterface;
        UserInterface user;
        RegionInterface regionInterface;
        RuntimeInterface runtimeInterface;

        logger.info("Deploying function {} to provider {}", deployFunction.getName(), deployFunction.getProvider());

        switch (deployFunction.getProvider()) {
            case AWS -> {
                deployInterface = new DeployAWS();
                user = UserAWS.fromResources("aws.properties"); // TODO: this has to come from the request in the future!
                regionInterface = RegionAWS.valueOf(deployFunction.getRegion());
                runtimeInterface = RuntimeAWS.valueOf(deployFunction.getRuntime());
            }
            case GCP -> {
                deployInterface = new DeployGCP();
                user = UserGCP.fromResources("meedesoro.json"); // TODO: this has to come from the request in the future!
                regionInterface = RegionGCP.valueOf(deployFunction.getRegion());
                runtimeInterface = RuntimeGCP.valueOf(deployFunction.getRuntime());
            } default -> throw new HolisticFaaSException("Provider not supported");
        }

        Function function = new Function();

        // TODO: support for other file paths (currently only resources)
        function.setFilePath(FileUtil.getFilePathFromResourcesFile(deployFunction.getFilePath()));

        function.setName(deployFunction.getName());
        function.setMemory(deployFunction.getMemory());
        function.setTimeoutSecs(deployFunction.getTimeoutSecs());
        function.setHandler(deployFunction.getHandler());
        function.setRegionInterface(regionInterface);
        function.setRuntimeInterface(runtimeInterface);

        return deployInterface.deployFunction(function, user);

    }

}
