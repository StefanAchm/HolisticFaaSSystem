package com.asi.hms.service;

import com.asi.hms.enums.DeployStatus;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.APIFunctionDeployment;
import com.asi.hms.model.db.DBFunctionDeployment;
import com.asi.hms.model.db.DBFunctionImplementation;
import com.asi.hms.model.db.DBUser;
import com.asi.hms.model.db.DBUserCredentials;
import com.asi.hms.repository.FunctionDeploymentRepository;
import com.asi.hms.repository.FunctionImplementationRepository;
import com.asi.hms.repository.UserCredentialsRepository;
import com.asi.hms.repository.UserRepository;
import com.asi.hms.utils.ProgressHandler;
import com.asi.hms.utils.cloudproviderutils.deploy.DeployInterface;
import com.asi.hms.enums.Provider;
import com.asi.hms.utils.cloudproviderutils.model.Function;
import com.asi.hms.model.UserInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FunctionDeploymentService {

    private static final Logger logger = LoggerFactory.getLogger(FunctionDeploymentService.class);

    private final UserRepository userRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final FunctionImplementationRepository functionImplementationRepository;
    private final FunctionDeploymentRepository functionDeploymentRepository;

    private final WebSocketSessionService sessionService;

    public FunctionDeploymentService(UserRepository userRepository,
                                     UserCredentialsRepository userCredentialsRepository,
                                     FunctionImplementationRepository functionImplementationRepository,
                                     FunctionDeploymentRepository functionDeploymentRepository,
                                     WebSocketSessionService sessionService) {

        this.userRepository = userRepository;
        this.userCredentialsRepository = userCredentialsRepository;
        this.functionImplementationRepository = functionImplementationRepository;
        this.functionDeploymentRepository = functionDeploymentRepository;
        this.sessionService = sessionService;

    }

    public void addFunctionDeployment(APIFunctionDeployment apiFunctionDeployment) {

        DBUser user = this.userRepository
                .findById(apiFunctionDeployment.getUserId())
                .orElseThrow(() -> new HolisticFaaSException("User '" + apiFunctionDeployment.getUserId() + "'not found"));

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

    public List<APIFunctionDeployment> getAllFunctionDeployments() {

        return this.functionDeploymentRepository.findAll().stream().map(APIFunctionDeployment::fromDBFunctionDeployment).toList();

    }


    public APIFunctionDeployment getFunctionDeployment(UUID functionId) {

        return APIFunctionDeployment.fromDBFunctionDeployment(this.functionDeploymentRepository.findById(functionId).orElseThrow());

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Deployment logic:

    @Async
    public void deploy(UUID functionDeploymentId) {

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

            DBUserCredentials userCredentials = this.userCredentialsRepository
                    .findDBUserCredentialsByUserAndProvider(
                            dbFunctionDeployment.getUser(),
                            dbFunctionDeployment.getProvider()
                    )
                    .orElseThrow(() -> new HolisticFaaSException("User credentials not found"));

            Function function = Function.fromDbFunction(dbFunctionDeployment);

            boolean success = deploy(userCredentials, dbFunctionDeployment, function, progressHandler);

            dbFunctionDeployment.setStatus(success ? DeployStatus.DEPLOYED : DeployStatus.FAILED);
            dbFunctionDeployment.setStatusMessage(success ? "Deployed successfully" : "Failed to deploy");


        } catch (HolisticFaaSException e) {

            dbFunctionDeployment.setStatusMessage(e.getMessage());
            dbFunctionDeployment.setStatus(DeployStatus.FAILED);

            logger.error("Error deploying function", e);

        }

        this.functionDeploymentRepository.save(dbFunctionDeployment);
        progressHandler.finish(dbFunctionDeployment.getStatusMessage());

    }

    protected static boolean deploy(DBUserCredentials dbUserCredentials,
                                    DBFunctionDeployment dbFunctionDeployment,
                                    Function function,
                                    ProgressHandler progressHandler) {

        Provider provider = Provider.valueOf(dbFunctionDeployment.getProvider());
        UserInterface userFromFile = provider.getUserFromFile(Paths.get(dbUserCredentials.getCredentialsFilePath()));
        DeployInterface deployer = provider.getDeployer();

        logger.info("Deploying function {} to provider {} at region {} for user {}",
                function.getName(),
                provider,
                function.getRegion(),
                dbFunctionDeployment.getUser().getUsername());

        return deployer.deployFunction(function, userFromFile, progressHandler);

    }

}
