package com.asi.hms.config.db;

import com.asi.hms.enums.DeployStatus;
import com.asi.hms.model.api.APIFunction2;
import com.asi.hms.model.api.APIWorkflow;
import com.asi.hms.model.db.*;
import com.asi.hms.repository.*;
import com.asi.hms.enums.Provider;
import com.asi.hms.service.FunctionService;
import com.asi.hms.utils.cloudproviderutils.afcl.AfclParser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DatabaseInitializer {

    private final String[] implementations = {
            "zips\\aws-helloworld-java-1.0-SNAPSHOT.zip",
            "zips\\gcp-helloworld-java-1.0-SNAPSHOT.zip",
            "zips\\multi-helloworld-java-1.0-SNAPSHOT.zip",
            "afcl_workflows\\genome\\genomeAwsFunctions\\genome_individual.py",
            "afcl_workflows\\genome\\genomeAwsFunctions\\genome_individuals_merge.py",
            "afcl_workflows\\genome\\genomeAwsFunctions\\genome_sifting.py",
            "afcl_workflows\\genome\\genomeAwsFunctions\\mutual_overlap\\zip.py",
            "afcl_workflows\\genome\\genomeAwsFunctions\\frequency\\zip.py",
    };

    private final String[] handlers = {
            "com.asi.hsg.HelloWorldHandler::handleRequest",
            "com.asi.hsg.HelloWorldHandler",
    };

    private final String[] credentials = {
            "auth\\stefan01\\aws.properties",
            "auth\\stefan01\\gcp.json"
    };

    private final String[] workflows = {
            "afcl_workflows\\genome\\genome.yaml"
    };

    private final String resourcesPath = "C:\\Users\\Stefan\\Documents\\git\\HolisticFaaS\\holistic-faas-management-system\\src\\main\\resources\\";

    private final PasswordEncoder passwordEncoder;
    private final FunctionDeploymentRepository functionDeploymentRepository;
    private final FunctionImplementationRepository functionImplementationRepository;
    private final FunctionTypeRepository functionTypeRepository;
    private final FunctionRepository functionRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final UserRepository userRepository;
    private final WorkflowRepository workflowRepository;
    private final WorkflowFunctionRepository workflowFunctionRepository;
    private final WorkflowDeploymentRepository workflowDeploymentRepository;

    private final FunctionService functionService;

    public DatabaseInitializer(PasswordEncoder passwordEncoder,
                               FunctionDeploymentRepository functionDeploymentRepository,
                               FunctionImplementationRepository functionImplementationRepository,
                               FunctionTypeRepository functionTypeRepository,
                               FunctionRepository functionRepository,
                               UserCredentialsRepository userCredentialsRepository,
                               UserRepository userRepository,
                               WorkflowRepository workflowRepository,
                               WorkflowFunctionRepository workflowFunctionRepository,
                               WorkflowDeploymentRepository workflowDeploymentRepository,
                               FunctionService functionService
    ) {

        this.passwordEncoder = passwordEncoder;

        this.functionDeploymentRepository = functionDeploymentRepository;
        this.functionImplementationRepository = functionImplementationRepository;
        this.functionTypeRepository = functionTypeRepository;
        this.functionRepository = functionRepository;

        this.userCredentialsRepository = userCredentialsRepository;
        this.userRepository = userRepository;

        this.workflowRepository = workflowRepository;
        this.workflowFunctionRepository = workflowFunctionRepository;
        this.workflowDeploymentRepository = workflowDeploymentRepository;

        this.functionService = functionService;

    }

    @PostConstruct
    public void init() {

        DBUser user1 = addUser("user1", "password");

        addUserCredentials(user1, "AWS", credentials[0]);
        addUserCredentials(user1, "GCP", credentials[1]);

//        addFunction(user1, "helloWorld1", implementations[0], handlers[0], Provider.AWS, 5, true);
//        addFunction(user1, "helloWorld2", implementations[1], handlers[1], Provider.GCP, 2, false);

//        List<APIFunction> allFunctions = this.functionService.getAllFunctions();

        DBUser user2 = addUser("user2", "password");
        addUserCredentials(user2, "AWS", credentials[0]);
        addUserCredentials(user2, "GCP", credentials[1]);

        DBUser user3 = addUser("user3", "password");

        DBWorkflow workflow1 = addAbstractWorkflowFromFile(workflows[0]);
        List<DBFunctionImplementation> dbFunctionImplementations = addWorkflowImplementations(Map.of(
                        implementations[3], workflow1.getFunctions().get(0).getFunctionType(),
                        implementations[4], workflow1.getFunctions().get(1).getFunctionType(),
                        implementations[5], workflow1.getFunctions().get(2).getFunctionType(),
                        implementations[6], workflow1.getFunctions().get(3).getFunctionType(),
                        implementations[7], workflow1.getFunctions().get(4).getFunctionType()
                )
        );

        DBFunctionDeployment dbFunctionDeployment1 = getDbFunctionDeployment(user1, handlers[0], Provider.AWS, dbFunctionImplementations.get(0), true);
        DBFunctionDeployment dbFunctionDeployment2 = getDbFunctionDeployment(user1, handlers[0], Provider.AWS, dbFunctionImplementations.get(1), true);
        DBFunctionDeployment dbFunctionDeployment3 = getDbFunctionDeployment(user1, handlers[0], Provider.AWS, dbFunctionImplementations.get(2), true);
        DBFunctionDeployment dbFunctionDeployment4 = getDbFunctionDeployment(user1, handlers[0], Provider.AWS, dbFunctionImplementations.get(3), true);
        DBFunctionDeployment dbFunctionDeployment5 = getDbFunctionDeployment(user1, handlers[0], Provider.AWS, dbFunctionImplementations.get(4), true);

        for(int i = 0; i < 5; i++) {
            addWorkflowDeployment("deployment" + i, user1, workflow1, Map.of(
                    workflow1.getFunctions().get(0), dbFunctionDeployment1,
                    workflow1.getFunctions().get(1), dbFunctionDeployment2,
                    workflow1.getFunctions().get(2), dbFunctionDeployment3,
                    workflow1.getFunctions().get(3), dbFunctionDeployment4,
                    workflow1.getFunctions().get(4), dbFunctionDeployment5
            ));
        }



    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Workflows

    private List<DBFunctionImplementation> addWorkflowImplementations(Map<String, DBFunctionType> implementation) {

        List<DBFunctionImplementation> dbFunctionImplementations = new ArrayList<>();
        implementation.forEach((functionImplementation, functionType) -> {

            DBFunctionImplementation dbFunctionImplementation = new DBFunctionImplementation();
            dbFunctionImplementation.setFilePath(resourcesPath + functionImplementation);
            dbFunctionImplementation.setFunctionType(functionType);

            functionImplementationRepository.save(dbFunctionImplementation);

            dbFunctionImplementations.add(dbFunctionImplementation);

        });

        return dbFunctionImplementations;

    }

    private void addWorkflowDeployment(String name, DBUser user1, DBWorkflow workflow, Map<DBFunction, DBFunctionDeployment> functionDeployments) {

        DBWorkflowDeployment workflowDeployment = new DBWorkflowDeployment();
        workflowDeployment.setWorkflow(workflow);
        workflowDeployment.setName(name);
        workflowDeployment.setUser(user1);
        workflowDeploymentRepository.save(workflowDeployment);

        functionDeployments.forEach((function, functionDeployment) -> {

            functionDeployment.setWorkflowDeployment(workflowDeployment);

            functionDeploymentRepository.save(functionDeployment);

        });

    }

    private DBWorkflow addAbstractWorkflowFromFile(String fileName) {

        APIWorkflow workflow = AfclParser.getWorkflow(resourcesPath + fileName);

        DBWorkflow dbWorkflow = DBWorkflow.fromAPIWorkflow(workflow);
        this.workflowRepository.save(dbWorkflow);

        dbWorkflow.setFunctions(new ArrayList<>());

        for (APIFunction2 apiFunction : workflow.getFunctions()) {

            DBFunctionType dbFunctionType = DBFunctionType.fromAPIFunctionType(apiFunction.getFunctionType());
            this.functionTypeRepository.save(dbFunctionType);

            DBFunction dbFunction = DBFunction.fromAPIFunction(apiFunction);
            dbFunction.setWorkflow(dbWorkflow);
            dbFunction.setFunctionType(dbFunctionType);
            this.functionRepository.save(dbFunction);

            dbFunction.setFunctionType(dbFunctionType);
            dbWorkflow.getFunctions().add(dbFunction);

        }

        return dbWorkflow;

    }

    private DBWorkflow addAbstractWorkflow(String workflowName, Map<String, String> functions) {

        DBWorkflow workflow = addWorkflow(workflowName);

        workflow.setFunctions(new ArrayList<>());

        functions.forEach((functionName, functionType) -> {

            DBFunctionType dbFunctionType = addFunctionType(workflow, functionType);

            DBFunction workflowFunction = addWorkflowFunction(workflow, functionName, dbFunctionType);
            workflow.getFunctions().add(workflowFunction);

        });

        return workflow;

    }

    private DBWorkflow addWorkflow(String workflowName) {

        DBWorkflow workflow = new DBWorkflow();
        workflow.setName(workflowName);
        workflow.setDescription("description1");

        return workflowRepository.save(workflow);

    }

    private DBFunction addWorkflowFunction(DBWorkflow workflow, String functionName, DBFunctionType dbFunctionType) {

        DBFunction workflowFunction = new DBFunction();
        workflowFunction.setName(functionName);
        workflowFunction.setWorkflow(workflow);
        workflowFunction.setFunctionType(dbFunctionType);

        return workflowFunctionRepository.save(workflowFunction);

    }

    private DBFunctionType addFunctionType(DBWorkflow workflow, String functionType) {

        DBFunctionType dbFunctionType = new DBFunctionType();
        dbFunctionType.setName(functionType);
//        dbFunctionType.setWorkflow(workflow);

        return functionTypeRepository.save(dbFunctionType);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Users

    private DBUser addUser(String username, String password) {

        DBUser user = new DBUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);

    }

    private void addUserCredentials(DBUser user, String provider, String credentialsFilePath) {

        DBUserCredentials credentials = new DBUserCredentials();
        credentials.setCredentialsFilePath(resourcesPath + credentialsFilePath);
        credentials.setProvider(provider);
        credentials.setUser(user);

        userCredentialsRepository.save(credentials);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Functions

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
