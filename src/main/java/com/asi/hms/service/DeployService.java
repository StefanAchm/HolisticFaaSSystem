package com.asi.hms.service;

import com.asi.hms.enums.DeployStatus;
import com.asi.hms.enums.Provider;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.*;
import com.asi.hms.model.api.APIFunctionDeployment;
import com.asi.hms.model.db.DBFunction;
import com.asi.hms.model.db.DBFunctionDeployment;
import com.asi.hms.model.db.DBUser;
import com.asi.hms.repository.FunctionDeploymentRepository;
import com.asi.hms.repository.FunctionRepository;
import com.asi.hms.repository.UserRepository;
import com.asi.hms.utils.cloudproviderutils.DeployAWS;
import com.asi.hms.utils.cloudproviderutils.DeployGCP;
import com.asi.hms.utils.cloudproviderutils.DeployInterface;
import com.asi.hms.utils.cloudproviderutils.MessageInterface;
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
public class DeployService {

    private static final Logger logger = LoggerFactory.getLogger(DeployService.class);

    private final UserRepository userRepository;
    private final FunctionRepository functionRepository;
    private final FunctionDeploymentRepository functionDeploymentRepository;

    private final WebSocketSessionService sessionService;

    public DeployService(UserRepository userRepository,
                         FunctionRepository functionRepository,
                         FunctionDeploymentRepository functionDeploymentRepository,
                         WebSocketSessionService sessionService) {

        this.userRepository = userRepository;
        this.functionRepository = functionRepository;
        this.functionDeploymentRepository = functionDeploymentRepository;
        this.sessionService = sessionService;

    }

    public void addFunctionDeployment(APIFunctionDeployment apiFunctionDeployment) {

        DBFunctionDeployment dbFunctionDeployment = new DBFunctionDeployment();

        dbFunctionDeployment.setProvider(apiFunctionDeployment.getProvider().toString());
        dbFunctionDeployment.setMemory(apiFunctionDeployment.getMemory());
        dbFunctionDeployment.setTimeoutSecs(apiFunctionDeployment.getTimeoutSecs());
        dbFunctionDeployment.setHandler(apiFunctionDeployment.getHandler());
        dbFunctionDeployment.setRegion(apiFunctionDeployment.getRegion());
        dbFunctionDeployment.setRuntime(apiFunctionDeployment.getRuntime());

        DBUser user = userRepository.findByUsername(apiFunctionDeployment.getUserName());

        if (user == null) {
            throw new HolisticFaaSException("User '" + apiFunctionDeployment.getUserName() + "' not found");
        }

        dbFunctionDeployment.setUser(user);

        DBFunction function = functionRepository
                .findById(apiFunctionDeployment.getFunctionId())
                .orElseThrow(() -> new HolisticFaaSException("Function not found"));

        dbFunctionDeployment.setFunction(function);

        this.functionDeploymentRepository.save(dbFunctionDeployment);

    }

    @Async
    public void deploy(UUID functionDeploymentId, boolean localOnly) {

        Optional<DBFunctionDeployment> byId = this.functionDeploymentRepository.findById(functionDeploymentId);

        if (byId.isEmpty()) {
            throw new HolisticFaaSException("Function not found");
        }

        DBFunctionDeployment dbFunctionDeployment = byId.get();

        Function function = Function.fromDbFunction(dbFunctionDeployment);

        dbFunctionDeployment.setStatus(DeployStatus.STARTED);
        this.functionDeploymentRepository.save(dbFunctionDeployment);

        boolean success = false;

        try {

            if(!localOnly) {

                success = deploy(dbFunctionDeployment, function, this.sessionService);

            } else {

                success = localDeployTesting(dbFunctionDeployment, this.sessionService);

            }

        } catch (HolisticFaaSException e) {

            dbFunctionDeployment.setStatus(DeployStatus.FAILED);
            this.functionDeploymentRepository.save(dbFunctionDeployment);

        }

        dbFunctionDeployment.setStatus(success ? DeployStatus.DEPLOYED : DeployStatus.FAILED);
        this.functionDeploymentRepository.save(dbFunctionDeployment);

    }

    public List<APIFunctionDeployment> getAllFunctionDeployments() {

        return this.functionDeploymentRepository.findAll().stream().map(APIFunctionDeployment::fromDBFunctionDeployment).toList();

    }

    protected static boolean deploy(DBFunctionDeployment dbFunctionDeployment,
                                    Function function,
                                    MessageInterface messageInterface) {

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

        return deployer.deployFunction(function, user, messageInterface);

    }

    private boolean localDeployTesting(DBFunctionDeployment dbFunctionDeployment, WebSocketSessionService sessionService) {

        for(int i = 1; i <= 7; i++) {

            sessionService.sendMessage(new Message(dbFunctionDeployment.getId(), i, 7, "Step " + i));

            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        return Math.random() > 0.5;

    }

}
