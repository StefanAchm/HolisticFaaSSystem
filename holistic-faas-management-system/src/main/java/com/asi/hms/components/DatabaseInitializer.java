package com.asi.hms.components;

import com.asi.hms.enums.DeployStatus;
import com.asi.hms.model.db.*;
import com.asi.hms.repository.*;
import com.asi.hms.utils.cloudproviderutils.enums.RegionAWS;
import com.asi.hms.utils.cloudproviderutils.enums.RuntimeAWS;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    private final FunctionDeploymentRepository functionDeploymentRepository;
    private final FunctionImplementationRepository functionImplementationRepository;
    private final FunctionTypeRepository functionTypeRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final UserRepository userRepository;

    public DatabaseInitializer(FunctionDeploymentRepository functionDeploymentRepository,
                               FunctionImplementationRepository functionImplementationRepository,
                               FunctionTypeRepository functionTypeRepository,
                               UserCredentialsRepository userCredentialsRepository,
                                 UserRepository userRepository
    ) {

        this.functionDeploymentRepository = functionDeploymentRepository;
        this.functionImplementationRepository = functionImplementationRepository;
        this.functionTypeRepository = functionTypeRepository;

        this.userCredentialsRepository = userCredentialsRepository;
        this.userRepository = userRepository;

    }

    @PostConstruct
    private void init() {

        DBUser user1 = new DBUser();
        user1.setUsername("user1");

        userRepository.save(user1);


        DBUserCredentials credentials1 = new DBUserCredentials();
        credentials1.setCredentialsFilePath("holistic-faas-management-system\\src\\main\\resources\\auth\\stefan01\\aws.properties");
        credentials1.setProvider("AWS");
        credentials1.setUser(user1);

        userCredentialsRepository.save(credentials1);

        DBUserCredentials credentials2 = new DBUserCredentials();
        credentials2.setCredentialsFilePath("holistic-faas-management-system\\src\\main\\resources\\auth\\stefan01\\gcp.json");
        credentials2.setProvider("GCP");
        credentials2.setUser(user1);

        userCredentialsRepository.save(credentials2);

        DBFunctionType dbFunctionType = new DBFunctionType();
        dbFunctionType.setName("function01");

        functionTypeRepository.save(dbFunctionType);

        DBFunctionImplementation dbFunctionImplementation = new DBFunctionImplementation();
        dbFunctionImplementation.setFilePath("path/to/file");
        dbFunctionImplementation.setFunctionType(dbFunctionType);

        functionImplementationRepository.save(dbFunctionImplementation);

        for(int i = 0; i < 10; i++) {

            DBFunctionDeployment dbFunctionDeployment = new DBFunctionDeployment();
            dbFunctionDeployment.setProvider("AWS");
            dbFunctionDeployment.setUser(user1);
            dbFunctionDeployment.setRegion(RegionAWS.EU_WEST_2.getRegionCode());
            dbFunctionDeployment.setRuntime(RuntimeAWS.JAVA_17.getRuntimeCode());
            dbFunctionDeployment.setMemory(128 + i * 10);
            dbFunctionDeployment.setHandler("com.asi.hsg.HelloWorldHandler::handleRequest");
            dbFunctionDeployment.setStatus(DeployStatus.CREATED);
            dbFunctionDeployment.setTimeoutSecs(3 + i);
            dbFunctionDeployment.setFunctionImplementation(dbFunctionImplementation);

            functionDeploymentRepository.save(dbFunctionDeployment);

        }

    }

}
