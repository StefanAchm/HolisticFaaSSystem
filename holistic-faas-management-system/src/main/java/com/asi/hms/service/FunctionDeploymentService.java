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
import com.asi.hms.utils.cloudproviderutils.deploy.DeployerInterface;
import com.asi.hms.utils.cloudproviderutils.model.Function;
import com.asi.hms.model.UserInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.List;
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

    public void add(APIFunctionDeployment apiFunctionDeployment) {

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

    public void update(APIFunctionDeployment apiFunctionDeployment) {

        DBFunctionDeployment functionDeployment = this.functionDeploymentRepository
                .findById(apiFunctionDeployment.getId())
                .orElseThrow(() -> new HolisticFaaSException("Function deployment not found"));

        if(functionDeployment.getStatus().equals(DeployStatus.DEPLOYED) || functionDeployment.getStatus().equals(DeployStatus.FAILED)) {
            functionDeployment.setStatusWithMessage(DeployStatus.CHANGED, "Function deployment changed");
        }

        functionDeployment.setProvider(apiFunctionDeployment.getProvider().toString());
        functionDeployment.setMemory(apiFunctionDeployment.getMemory());
        functionDeployment.setTimeoutSecs(apiFunctionDeployment.getTimeoutSecs());
        functionDeployment.setHandler(apiFunctionDeployment.getHandler());
        functionDeployment.setRegion(apiFunctionDeployment.getRegion());
        functionDeployment.setRuntime(apiFunctionDeployment.getRuntime());

        this.functionDeploymentRepository.save(functionDeployment);

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

        DBFunctionDeployment dbFunctionDeployment = getDbFunctionDeployment(functionDeploymentId);
        DBUserCredentials userCredentials = getDbUserCredentials(dbFunctionDeployment);

        Function function = Function.fromDbFunction(dbFunctionDeployment);
        ProgressHandler progressHandler = function.getProvider().getProgressHandler(dbFunctionDeployment, this.sessionService);

        boolean isUpdate = dbFunctionDeployment.getStatus().canUpdate();
        String action = isUpdate ? "Update" : "Deployment";

        try {

            // Start deployment
            dbFunctionDeployment.setStatusWithMessage(DeployStatus.STARTED, action + " started");
            this.functionDeploymentRepository.save(dbFunctionDeployment);

            progressHandler.start(
                    String.format("%s of function %s to provider %s at region %s for user %s",
                            action,
                            function.getName(),
                            function.getProvider(),
                            function.getRegion(),
                            userCredentials.getUser().getUsername()
                    )
            );

            UserInterface user = function.getProvider().getUserFromFile(Paths.get(userCredentials.getCredentialsFilePath()));
            DeployerInterface deployer = function.getProvider().getDeployer(user);

            // Actual deployment
            boolean success = isUpdate
                    ? deployer.updateFunction(function, progressHandler)
                    : deployer.deployFunction(function, progressHandler);


            dbFunctionDeployment.setStatusWithMessage(
                    success ? DeployStatus.DEPLOYED : DeployStatus.FAILED,
                    action + " " + (success ? "success" : "failed")
            );

        } catch (HolisticFaaSException e) {

            dbFunctionDeployment.setStatusWithMessage(DeployStatus.FAILED, e.getMessage());
            logger.error("{} error:", action, e);

        } finally {

            this.functionDeploymentRepository.save(dbFunctionDeployment);
            progressHandler.finish(dbFunctionDeployment.getStatusMessage());

        }

    }

    private DBUserCredentials getDbUserCredentials(DBFunctionDeployment dbFunctionDeployment) {

        return this.userCredentialsRepository
                .findDBUserCredentialsByUserAndProvider(
                        dbFunctionDeployment.getUser(),
                        dbFunctionDeployment.getProvider()
                )
                .orElseThrow(() -> new HolisticFaaSException("User credentials not found"));
    }

    private DBFunctionDeployment getDbFunctionDeployment(UUID functionDeploymentId) {

        return this.functionDeploymentRepository.findById(functionDeploymentId)
                .orElseThrow(() -> new HolisticFaaSException("Function deployment not found"));

    }

}
