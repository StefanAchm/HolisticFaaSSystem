package com.asi.hms.config.db;

import com.asi.hms.enums.DeployStatus;
import com.asi.hms.model.db.*;
import com.asi.hms.repository.*;
import com.asi.hms.enums.Provider;
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

    private final String[] credentials = {
            "auth\\stefan01\\aws.properties",
            "auth\\stefan01\\gcp.json"
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

        DBUser user1 = addUser("user1", "password");

        addUserCredentials(user1, "AWS", credentials[0]);
        addUserCredentials(user1, "GCP", credentials[1]);

        addFunction(user1, "function1", implementations[0], handlers[0], Provider.AWS, 5, true);
        addFunction(user1, "function2", implementations[1], handlers[1], Provider.GCP, 2, false);

        DBUser user2 = addUser("user2", "password");
        addUserCredentials(user2, "AWS", credentials[0]);

        DBUser user3 = addUser("user3", "password");

    }

    private DBUser addUser(String username, String password) {

        DBUser user = new DBUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        return user;

    }

    private void addUserCredentials(DBUser user, String provider, String credentialsFilePath) {

        DBUserCredentials credentials = new DBUserCredentials();
        credentials.setCredentialsFilePath(resourcesPath + credentialsFilePath);
        credentials.setProvider(provider);
        credentials.setUser(user);

        userCredentialsRepository.save(credentials);

    }


    private void addFunction(DBUser user,
                             String functionName,
                             String implementationPath,
                             String handlerPath,
                             Provider provider,
                             int nrOfDeployments,
                             boolean random) {

        DBFunctionType dbFunctionType = new DBFunctionType();
        dbFunctionType.setName(functionName);

        functionTypeRepository.save(dbFunctionType);

        DBFunctionImplementation dbFunctionImplementation = new DBFunctionImplementation();
        dbFunctionImplementation.setFilePath(resourcesPath + implementationPath);
        dbFunctionImplementation.setFunctionType(dbFunctionType);

        functionImplementationRepository.save(dbFunctionImplementation);

        for (int i = 0; i < nrOfDeployments; i++) {

            DBFunctionDeployment dbFunctionDeployment = getDbFunctionDeployment(user, handlerPath, provider, dbFunctionImplementation, random);

            functionDeploymentRepository.save(dbFunctionDeployment);

        }

    }

    private static DBFunctionDeployment getDbFunctionDeployment(DBUser user,
                                                                String handlerPath,
                                                                Provider provider,
                                                                DBFunctionImplementation dbFunctionImplementation,
                                                                boolean random) {

        DBFunctionDeployment dbFunctionDeployment = new DBFunctionDeployment();

        dbFunctionDeployment.setProvider(provider.name());
        dbFunctionDeployment.setUser(user);
        dbFunctionDeployment.setRegion(provider == Provider.AWS ? "eu-west-2" : "europe-west2");
        dbFunctionDeployment.setRuntime("java17");

        dbFunctionDeployment.setMemory(random ? (int) (Math.random() * 10) + 128 : 128);
        dbFunctionDeployment.setHandler(handlerPath);
        dbFunctionDeployment.setStatus(DeployStatus.CREATED);
        dbFunctionDeployment.setTimeoutSecs(random ? (int) (Math.random() * 10) + 3 : 3);
        dbFunctionDeployment.setFunctionImplementation(dbFunctionImplementation);

        return dbFunctionDeployment;

    }

}
