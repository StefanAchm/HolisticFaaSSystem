package com.asi.hms.components;

import com.asi.hms.enums.DeployStatus;
import com.asi.hms.model.db.*;
import com.asi.hms.repository.*;
import com.asi.hms.utils.cloudproviderutils.enums.Provider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    private final String[] implementations = {
            "zips\\aws-helloworld-java-1.0-SNAPSHOT.zip",
            "zips\\gcp-helloworld-java-1.0-SNAPSHOT.zip",
            "zips\\multi-helloworld-java-1.0-SNAPSHOT.zip"
    };

    private final String[] handlers = {
            "com.asi.hsg.HelloWorldHandler::handleRequest",
            "com.asi.hsg.HelloWorldHandler",
    };

    private final String resourcesPath = "C:\\Users\\Stefan\\Documents\\git\\HolisticFaaS\\holistic-faas-management-system\\src\\main\\resources\\";

    private final PasswordEncoder passwordEncoder;
    private final FunctionDeploymentRepository functionDeploymentRepository;
    private final FunctionImplementationRepository functionImplementationRepository;
    private final FunctionTypeRepository functionTypeRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final UserRepository userRepository;

    public DatabaseInitializer(PasswordEncoder passwordEncoder,
                               FunctionDeploymentRepository functionDeploymentRepository,
                               FunctionImplementationRepository functionImplementationRepository,
                               FunctionTypeRepository functionTypeRepository,
                               UserCredentialsRepository userCredentialsRepository,
                               UserRepository userRepository
    ) {

        this.passwordEncoder = passwordEncoder;

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
        user1.setPassword(passwordEncoder.encode("password"));

        userRepository.save(user1);


        DBUserCredentials credentials1 = new DBUserCredentials();
        credentials1.setCredentialsFilePath(resourcesPath + "auth\\stefan01\\aws.properties");
        credentials1.setProvider("AWS");
        credentials1.setUser(user1);

        userCredentialsRepository.save(credentials1);

        DBUserCredentials credentials2 = new DBUserCredentials();
        credentials2.setCredentialsFilePath(resourcesPath + "auth\\stefan01\\gcp.json");
        credentials2.setProvider("GCP");
        credentials2.setUser(user1);

        userCredentialsRepository.save(credentials2);

        addFunction(user1, "function1", implementations[0], handlers[0], Provider.AWS, 5);
        addFunction(user1, "function2", implementations[1], handlers[1], Provider.GCP, 2);

    }


    private void addFunction(DBUser user,
                             String functionName,
                             String implementationPath,
                             String handlerPath,
                             Provider provider,
                             int nrOfDeployments) {

        DBFunctionType dbFunctionType = new DBFunctionType();
        dbFunctionType.setName(functionName);

        functionTypeRepository.save(dbFunctionType);

        DBFunctionImplementation dbFunctionImplementation = new DBFunctionImplementation();
        dbFunctionImplementation.setFilePath(resourcesPath + implementationPath);
        dbFunctionImplementation.setFunctionType(dbFunctionType);

        functionImplementationRepository.save(dbFunctionImplementation);

        for (int i = 0; i < nrOfDeployments; i++) {

            DBFunctionDeployment dbFunctionDeployment = getDbFunctionDeployment(user, handlerPath, provider, dbFunctionImplementation);

            functionDeploymentRepository.save(dbFunctionDeployment);

        }

    }

    private static DBFunctionDeployment getDbFunctionDeployment(DBUser user,
                                                                String handlerPath,
                                                                Provider provider,
                                                                DBFunctionImplementation dbFunctionImplementation) {

        DBFunctionDeployment dbFunctionDeployment = new DBFunctionDeployment();

        dbFunctionDeployment.setProvider(provider.name());
        dbFunctionDeployment.setUser(user);
        dbFunctionDeployment.setRegion("eu-west-2");
        dbFunctionDeployment.setRuntime("java17");
        dbFunctionDeployment.setMemory(128);
        dbFunctionDeployment.setHandler(handlerPath);
        dbFunctionDeployment.setStatus(DeployStatus.CREATED);
        dbFunctionDeployment.setTimeoutSecs(3);
        dbFunctionDeployment.setFunctionImplementation(dbFunctionImplementation);

        return dbFunctionDeployment;

    }

}
