package com.asi.hms.service;

import com.asi.hms.enums.DeployStatus;
import com.asi.hms.enums.Provider;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.Function;
import com.asi.hms.model.UserAWS;
import com.asi.hms.model.UserGCP;
import com.asi.hms.model.UserInterface;
import com.asi.hms.model.api.APIFunctionDeployment;
import com.asi.hms.model.db.DBFunctionImplementation;
import com.asi.hms.model.db.DBFunctionDeployment;
import com.asi.hms.model.db.DBUser;
import com.asi.hms.repository.FunctionDeploymentRepository;
import com.asi.hms.repository.FunctionImplementationRepository;
import com.asi.hms.repository.UserRepository;
import com.asi.hms.utils.ProgressHandler;
import com.asi.hms.utils.cloudproviderutils.DeployAWS;
import com.asi.hms.utils.cloudproviderutils.DeployGCP;
import com.asi.hms.utils.cloudproviderutils.DeployInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class FunctionDeploymentService {

    private static final Logger logger = LoggerFactory.getLogger(FunctionDeploymentService.class);

    private final UserRepository userRepository;
    private final FunctionImplementationRepository functionImplementationRepository;
    private final FunctionDeploymentRepository functionDeploymentRepository;

    private final WebSocketSessionService sessionService;

    public FunctionDeploymentService(UserRepository userRepository,
                                     FunctionImplementationRepository functionImplementationRepository,
                                     FunctionDeploymentRepository functionDeploymentRepository,
                                     WebSocketSessionService sessionService) {

        this.userRepository = userRepository;
        this.functionImplementationRepository = functionImplementationRepository;
        this.functionDeploymentRepository = functionDeploymentRepository;
        this.sessionService = sessionService;

    }

    public void addFunctionDeployment(APIFunctionDeployment apiFunctionDeployment) {

        DBUser user = this.userRepository.findByUsername(apiFunctionDeployment.getUserName());

        if (user == null) {
            throw new HolisticFaaSException("User '" + apiFunctionDeployment.getUserName() + "' not found");
        }

        DBFunctionImplementation functionImplementation = this.functionImplementationRepository
                .findById(apiFunctionDeployment.getFunctionImplementationId())
                .orElseThrow(() -> new HolisticFaaSException("Function not found"));

        DBFunctionDeployment dbFunctionDeployment = DBFunctionDeployment.fromAPIFunctionDeployment(
                apiFunctionDeployment,
                user,
                functionImplementation
        );

        this.functionDeploymentRepository.save(dbFunctionDeployment);

    }



    @Async
    public void deploy(UUID functionDeploymentId, boolean localOnly) {

        Optional<DBFunctionDeployment> byId = this.functionDeploymentRepository.findById(functionDeploymentId);

        if (byId.isEmpty()) {
            throw new HolisticFaaSException("Function not found");
        }

        DBFunctionDeployment dbFunctionDeployment = byId.get();

        dbFunctionDeployment.setStatus(DeployStatus.STARTED);
        dbFunctionDeployment.setStatusMessage("Deployment started");
        this.functionDeploymentRepository.save(dbFunctionDeployment);

        // TODO: Define steps dynamically
        ProgressHandler progressHandler = new ProgressHandler(dbFunctionDeployment, 6, this.sessionService);
        progressHandler.start();

        try {

            boolean success;

            if(!localOnly) {

                Function function = Function.fromDbFunction(dbFunctionDeployment);
                success = deploy(dbFunctionDeployment, function, progressHandler);

            } else {

                success = localDeployTesting(progressHandler);

            }

            dbFunctionDeployment.setStatus(success ? DeployStatus.DEPLOYED : DeployStatus.FAILED);
            dbFunctionDeployment.setStatusMessage(success ? "Deployed successfully" : "Failed to deploy");


        } catch (HolisticFaaSException e) {

            dbFunctionDeployment.setStatusMessage(e.getMessage());
            dbFunctionDeployment.setStatus(DeployStatus.FAILED);

            logger.error("Error deploying function", e);

        }

        this.functionDeploymentRepository.save(dbFunctionDeployment);
        progressHandler.finish();

    }

    public List<APIFunctionDeployment> getAllFunctionDeployments() {

        return this.functionDeploymentRepository.findAll().stream().map(APIFunctionDeployment::fromDBFunctionDeployment).toList();

    }

    protected static boolean deploy(DBFunctionDeployment dbFunctionDeployment,
                                    Function function,
                                    ProgressHandler progressHandler) {

        DeployInterface deployer;
        UserInterface user;

        Provider provider = Provider.valueOf(dbFunctionDeployment.getProvider());

        switch (provider) {
            case AWS -> {
                deployer = new DeployAWS();
                user = UserAWS.fromFile(Paths.get(dbFunctionDeployment.getUser().getCredentialsFilePath()));
            }
            case GCP -> {
                deployer = new DeployGCP();
                user = UserGCP.fromFile(Paths.get(dbFunctionDeployment.getUser().getCredentialsFilePath()));

            }

            default -> throw new HolisticFaaSException("Provider not supported");

        }

        logger.info("Deploying function {} to provider {} at region {} for user {}",
                function.getName(),
                provider,
                function.getRegion(),
                dbFunctionDeployment.getUser().getUsername());

        return deployer.deployFunction(function, user, progressHandler);

    }

    @Deprecated
    private boolean localDeployTesting(ProgressHandler progressHandler) {

        for(int i = 1; i <= 7; i++) {

            progressHandler.update("Step " + i);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        return Math.random() > 0.5;

    }

    public APIFunctionDeployment getFunctionDeployment(UUID functionId) {

        return APIFunctionDeployment.fromDBFunctionDeployment(this.functionDeploymentRepository.findById(functionId).orElseThrow());

    }

}
