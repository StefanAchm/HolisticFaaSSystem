package com.asi.hms.service;

import com.asi.hms.enums.*;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.*;
import com.asi.hms.model.api.APIFunction;
import com.asi.hms.model.db.DBFunction;
import com.asi.hms.repository.FunctionRepository;
import com.asi.hms.utils.FileUtil;
import com.asi.hms.utils.cloudproviderutils.DeployAWS;
import com.asi.hms.utils.cloudproviderutils.DeployGCP;
import com.asi.hms.utils.cloudproviderutils.DeployInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeployService {

    private static final Logger logger = LoggerFactory.getLogger(DeployService.class);

    private final FunctionRepository functionRepository;

    public DeployService(FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
    }

    public boolean deploy(UUID functionId) {

        Optional<DBFunction> byId = functionRepository.findById(functionId);

        if(byId.isEmpty()) {
            throw new HolisticFaaSException("Function not found");
        }

        DBFunction dbFunction = byId.get();

        APIFunction apiFunction = APIFunction.fromDBFunction(dbFunction);

        switch (apiFunction.getProvider()) {
            case AWS -> apiFunction.setCredentialsPath("auth/" + apiFunction.getUserName() + "/aws.properties");
            case GCP -> apiFunction.setCredentialsPath("auth/" + apiFunction.getUserName() + "/gcp.json");
            default -> throw new HolisticFaaSException("Provider not supported");
        }

        return deploy(apiFunction);

    }

    public boolean deploy(APIFunction apiFunction) {

        DeployInterface deployInterface;
        UserInterface user;
        RegionInterface regionInterface;
        RuntimeInterface runtimeInterface;

        logger.info("Deploying function {} to provider {} at region {} for user {}",
                apiFunction.getName(),
                apiFunction.getProvider(),
                apiFunction.getRegion(),
                apiFunction.getCredentialsPath());

        switch (apiFunction.getProvider()) {
            case AWS -> {
                deployInterface = new DeployAWS();
                user = UserAWS.fromResources(apiFunction.getCredentialsPath());
                regionInterface = RegionAWS.valueOf(apiFunction.getRegion());
                runtimeInterface = RuntimeAWS.valueOf(apiFunction.getRuntime());
            }
            case GCP -> {
                deployInterface = new DeployGCP();
                user = UserGCP.fromResources(apiFunction.getCredentialsPath());
                regionInterface = RegionGCP.valueOf(apiFunction.getRegion());
                runtimeInterface = RuntimeGCP.valueOf(apiFunction.getRuntime());
            } default -> throw new HolisticFaaSException("Provider not supported");
        }

        Function function = new Function();

        // TODO: support for other file paths (currently only resources)
        function.setFilePath(FileUtil.getFilePathFromResourcesFile(apiFunction.getFilePath()));

        function.setName(apiFunction.getName());
        function.setMemory(apiFunction.getMemory());
        function.setTimeoutSecs(apiFunction.getTimeoutSecs());
        function.setHandler(apiFunction.getHandler());
        function.setRegionInterface(regionInterface);
        function.setRuntimeInterface(runtimeInterface);

        return deployInterface.deployFunction(function, user);

    }

}
