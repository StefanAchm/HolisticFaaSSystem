package com.asi.hms.service;

import com.asi.hms.enums.DeployStatus;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.UserAWS;
import com.asi.hms.model.api.APIFunctionDeployment;
import com.asi.hms.model.db.*;
import com.asi.hms.repository.*;
import com.asi.hms.utils.ProgressHandler;
import com.asi.hms.utils.cloudproviderutils.deploy.DeployerInterface;
import com.asi.hms.utils.cloudproviderutils.model.Function;
import com.asi.hms.model.UserInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

@Service
public class FunctionDeploymentService {

    private static final Logger logger = LoggerFactory.getLogger(FunctionDeploymentService.class);

    private final UserRepository userRepository;
    private final UserCredentialsRepository userCredentialsRepository;

    private final FunctionImplementationRepository functionImplementationRepository;
    private final FunctionDeploymentRepository functionDeploymentRepository;
    private final FunctionRepository functionRepository;

    private final WebSocketSessionService sessionService;

    private final UserCredentialsService userCredentialsService;

    public FunctionDeploymentService(UserRepository userRepository,
                                     UserCredentialsRepository userCredentialsRepository,
                                     FunctionImplementationRepository functionImplementationRepository,
                                     FunctionDeploymentRepository functionDeploymentRepository,
                                     FunctionRepository functionRepository,
                                     WebSocketSessionService sessionService,
                                     UserCredentialsService userCredentialsService) {

        this.userRepository = userRepository;
        this.userCredentialsRepository = userCredentialsRepository;

        this.functionImplementationRepository = functionImplementationRepository;
        this.functionDeploymentRepository = functionDeploymentRepository;
        this.functionRepository = functionRepository;

        this.sessionService = sessionService;

        this.userCredentialsService = userCredentialsService;

    }

    public APIFunctionDeployment add(APIFunctionDeployment apiFunctionDeployment) {

        DBUser user = this.userRepository
                .findById(apiFunctionDeployment.getUserId())
                .orElseThrow(() -> new HolisticFaaSException("User '" + apiFunctionDeployment.getUserId() + "'not found"));

        DBFunctionImplementation functionImplementation = this.functionImplementationRepository
                .findById(apiFunctionDeployment.getFunctionImplementationId())
                .orElseThrow(() -> new HolisticFaaSException("Function not found"));

        DBFunction function = this.functionRepository
                .findById(apiFunctionDeployment.getFunctionId())
                .orElseThrow(() -> new HolisticFaaSException("Function not found"));

        DBFunctionDeployment dbFunctionDeployment = DBFunctionDeployment.fromAPIFunctionDeployment(
                apiFunctionDeployment,
                user,
                functionImplementation,
                function
        );

        this.functionDeploymentRepository.save(dbFunctionDeployment);

        return APIFunctionDeployment.fromDBFunctionDeployment(dbFunctionDeployment);

    }

    public APIFunctionDeployment update(APIFunctionDeployment apiFunctionDeployment) {

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

        return APIFunctionDeployment.fromDBFunctionDeployment(functionDeployment);

    }

    public List<APIFunctionDeployment> getAllFunctionDeployments() {

        return this.functionDeploymentRepository.findAll().stream().map(APIFunctionDeployment::fromDBFunctionDeployment).toList();

    }


    public APIFunctionDeployment getFunctionDeployment(UUID functionId) {

        return APIFunctionDeployment.fromDBFunctionDeployment(this.functionDeploymentRepository.findById(functionId).orElseThrow());

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Deployment logic:

    public boolean validateUser(UUID functionDeploymentId) {

        DBFunctionDeployment dbFunctionDeployment = getDbFunctionDeployment(functionDeploymentId);

        DBUser dbUser = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getDbUser();

        return dbUser.getId().equals(dbFunctionDeployment.getUser().getId());

    }

    @Async("deploymentExecutor")
    public void deploy(UUID functionDeploymentId, String awsSessionToken) {

        DBFunctionDeployment dbFunctionDeployment = getDbFunctionDeployment(functionDeploymentId);
        DBUser dbUser = dbFunctionDeployment.getUser();

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
                            dbUser.getUsername()
                    )
            );

            UserInterface user = function.getProvider().getUserFromInputStream(
                    new ByteArrayInputStream(
                            userCredentialsService.getCredentials(
                                    dbUser.getId(),
                                    dbFunctionDeployment.getProvider()
                            ).getBytes()
                    )
            );

            if(user instanceof UserAWS userAWS && awsSessionToken != null) {
                userAWS.setSessionToken(awsSessionToken);
            }

            DeployerInterface deployer = function.getProvider().getDeployer(user);

            // Actual deployment

            boolean success = false;
            if(isUpdate) {
                deployer.updateFunction(function, progressHandler);
                success = true;
            } else {
                String resource = deployer.deployFunction(function, progressHandler);
                if(resource != null && !resource.isEmpty()) {
                    dbFunctionDeployment.setResource(resource);
                    success = true;
                }
            }

            dbFunctionDeployment.setStatusWithMessage(
                    success ? DeployStatus.DEPLOYED : DeployStatus.FAILED,
                    action + " " + (success ? "success" : "failed")
            );

        } catch (HolisticFaaSException e) {

            dbFunctionDeployment.setStatusWithMessage(DeployStatus.FAILED, e.getMessage());
            logger.error("{} error:", action, e);

        } catch (Exception e) {

            dbFunctionDeployment.setStatusWithMessage(DeployStatus.FAILED, action + " error");
            logger.error("{} error:", action, e);

        } finally {

            this.functionDeploymentRepository.save(dbFunctionDeployment);
            progressHandler.finish(dbFunctionDeployment.getStatusMessage());

        }

    }

    public void prepareDeployment(UUID functionDeploymentId) {

        DBFunctionDeployment dbFunctionDeployment = getDbFunctionDeployment(functionDeploymentId);

        Function function = Function.fromDbFunction(dbFunctionDeployment);
        ProgressHandler progressHandler = function.getProvider().getProgressHandler(dbFunctionDeployment, this.sessionService);

        boolean isUpdate = dbFunctionDeployment.getStatus().canUpdate();
        String action = isUpdate ? "Update" : "Deployment";

        // Start deployment
        dbFunctionDeployment.setStatusWithMessage(DeployStatus.WAITING, action + " waiting");
        this.functionDeploymentRepository.save(dbFunctionDeployment);

        progressHandler.waiting();

    }

    private DBFunctionDeployment getDbFunctionDeployment(UUID functionDeploymentId) {

        return this.functionDeploymentRepository.findById(functionDeploymentId)
                .orElseThrow(() -> new HolisticFaaSException("Function deployment not found"));

    }

}
