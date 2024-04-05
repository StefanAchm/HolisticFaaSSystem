package com.asi.hms.components;

import com.asi.hms.enums.DeployStatus;
import com.asi.hms.model.db.DBFunctionDeployment;
import com.asi.hms.model.db.DBFunctionImplementation;
import com.asi.hms.model.db.DBFunctionType;
import com.asi.hms.model.db.DBUser;
import com.asi.hms.repository.FunctionDeploymentRepository;
import com.asi.hms.repository.FunctionImplementationRepository;
import com.asi.hms.repository.FunctionTypeRepository;
import com.asi.hms.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    private final FunctionDeploymentRepository functionDeploymentRepository;
    private final FunctionImplementationRepository functionImplementationRepository;
    private final FunctionTypeRepository functionTypeRepository;
    private final UserRepository userRepository;

    public DatabaseInitializer(FunctionDeploymentRepository functionDeploymentRepository,
                               FunctionImplementationRepository functionImplementationRepository,
                               FunctionTypeRepository functionTypeRepository,
                               UserRepository userRepository) {

        this.functionDeploymentRepository = functionDeploymentRepository;
        this.functionImplementationRepository = functionImplementationRepository;
        this.functionTypeRepository = functionTypeRepository;
        this.userRepository = userRepository;

    }

    @PostConstruct
    private void init() {

        DBUser user1 = new DBUser();
        user1.setCredentialsFilePath("holistic-faas-management-system\\src\\main\\resources\\auth\\stefan01\\aws.properties");
        user1.setProvider("AWS");
        user1.setUsername("user1");

        userRepository.save(user1);

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
            dbFunctionDeployment.setRegion("EU_WEST_2");
            dbFunctionDeployment.setRuntime("JAVA_17");
            dbFunctionDeployment.setMemory(128 + i * 10);
            dbFunctionDeployment.setHandler("com.asi.hsg.HelloWorldHandler::handleRequest");
            dbFunctionDeployment.setStatus(DeployStatus.CREATED);
            dbFunctionDeployment.setTimeoutSecs(3 + i);
            dbFunctionDeployment.setFunctionImplementation(dbFunctionImplementation);

            functionDeploymentRepository.save(dbFunctionDeployment);

        }


    }

}
