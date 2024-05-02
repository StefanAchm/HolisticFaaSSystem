package com.asi.hms.config.db;

import com.asi.hms.enums.*;
import com.asi.hms.model.api.APIFunction;
import com.asi.hms.model.api.APIWorkflow;
import com.asi.hms.model.db.*;
import com.asi.hms.repository.*;
import com.asi.hms.service.FunctionFlatService;
import com.asi.hms.service.UserService;
import com.asi.hms.utils.cloudproviderutils.afcl.AfclParser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class DatabaseInitializer {


    private final String[] implementations = {
            "zips\\aws-helloworld-java-1.0-SNAPSHOT.zip",
            "zips\\gcp-helloworld-java-1.0-SNAPSHOT.zip",
            "zips\\multi-helloworld-java-1.0-SNAPSHOT.zip",
            "afcl_workflows\\genome\\genomeAwsFunctions\\genome_individual.py",
            "afcl_workflows\\genome\\genomeAwsFunctions\\genome_individuals_merge.py",
            "afcl_workflows\\genome\\genomeAwsFunctions\\genome_sifting.py",
            "afcl_workflows\\genome\\genomeAwsFunctions\\mutual_overlap\\zip.zip",
            "afcl_workflows\\genome\\genomeAwsFunctions\\frequency\\zip.zip",
    };

    private final String[] handlers = {
            "com.asi.hsg.HelloWorldHandler::handleRequest",
            "com.asi.hsg.HelloWorldHandler",
            // lambda_function.lambda_handler

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

    private final UserService userService;

    private final FunctionFlatService functionFlatService;

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
                               UserService userService,
                               FunctionFlatService functionFlatService
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

        this.userService = userService;

        this.functionFlatService = functionFlatService;

    }

    @PostConstruct
    public void init() {

        DBUser user1 = addUser("user1", "password");

        addUserCredentials(user1, "AWS", credentials[0]);
        addUserCredentials(user1, "GCP", credentials[1]);

        UserDetails userDetails = userService.loadUserByUsername("user1");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


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


        for (int i = 0; i < 5; i++) {


            Map<DBFunction, DBFunctionDeployment> dbFunctionDeployments = new HashMap<>();
            for (DBFunction function : workflow1.getFunctions()) {

                String name = function.getFunctionType().getName();

                Optional<DBFunctionImplementation> first = dbFunctionImplementations
                        .stream()
                        .filter(impl -> impl.getFunctionType().getName().equals(name))
                        .findFirst();

                if (first.isPresent()) {

                    DBFunctionDeployment dbFunctionDeployment = getDbFunctionDeployment(user1,
                            handlers[0], // TODO
                            RuntimeAWS.PYTHON_3_12.getRuntimeCode(),
                            Provider.AWS,
                            first.get(),
                            function,
                            true);

                    dbFunctionDeployments.put(function, dbFunctionDeployment);

                }

            }

            addWorkflowDeployment("deployment" + i, user1, workflow1, dbFunctionDeployments);

        }


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Workflows

    private List<DBFunctionImplementation> addWorkflowImplementations(Map<String, DBFunctionType> implementation) {

        List<DBFunctionImplementation> dbFunctionImplementations = new ArrayList<>();
        for (Map.Entry<String, DBFunctionType> entry : implementation.entrySet()) {
            String functionImplementation = entry.getKey();
            DBFunctionType functionType = entry.getValue();
            DBFunctionImplementation dbFunctionImplementation = new DBFunctionImplementation();
            dbFunctionImplementation.setFilePath(resourcesPath + functionImplementation);
            dbFunctionImplementation.setFunctionType(functionType);

            functionImplementationRepository.save(dbFunctionImplementation);

            dbFunctionImplementations.add(dbFunctionImplementation);

        }

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
        workflow.setDescription("Genome1000 (GEN) is a scientific workflow which identifies mutational overlaps using data from the 1000 Genomes project in order to provide a null distribution for rigorous statistical evaluation of potential disease-related mutations. Each instance of the function Individual fetches and parses single nucleotide polymorphism (SNPs) variants in a chromosome and determines which individuals contain these variants. Individuals_merge merges all outputs of individuals, while Sifting computes the mutation for all SNP variants (SIFT scores). Next, Mutation_overlap measures SNP variants (the overlap in mutations), while Frequency measures the frequency of mutational overlaps. For every of the six super populations (such as African, Mixed American or East Asian) as well as for all populations, a separate instance of Mutations_overlap and Frequency is invoked in the last two parallel loops.");
        workflow.setFilePath(resourcesPath + fileName);

        DBWorkflow dbWorkflow = DBWorkflow.fromAPIWorkflow(workflow);
        this.workflowRepository.save(dbWorkflow);

        dbWorkflow.setFunctions(new ArrayList<>());

        for (APIFunction apiFunction : workflow.getFunctions()) {

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

            DBFunctionType dbFunctionType = addFunctionType(functionType);

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

    private DBFunctionType addFunctionType(String functionType) {

        DBFunctionType dbFunctionType = new DBFunctionType();
        dbFunctionType.setName(functionType);

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

            DBFunctionDeployment dbFunctionDeployment = getDbFunctionDeployment(user, handlerPath, "java17", provider, dbFunctionImplementation, null, random);

            functionDeploymentRepository.save(dbFunctionDeployment);

        }

    }

    private static DBFunctionDeployment getDbFunctionDeployment(DBUser user,
                                                                String handlerPath,
                                                                String runtime,
                                                                Provider provider,
                                                                DBFunctionImplementation dbFunctionImplementation,
                                                                DBFunction function,
                                                                boolean random) {

        DBFunctionDeployment dbFunctionDeployment = new DBFunctionDeployment();

        dbFunctionDeployment.setProvider(provider.name());
        dbFunctionDeployment.setUser(user);
        dbFunctionDeployment.setRegion(provider == Provider.AWS ? "eu-west-2" : "europe-west2");
        dbFunctionDeployment.setRuntime(runtime);

        dbFunctionDeployment.setMemory(random ? (int) (Math.random() * 10) + 128 : 128);
        dbFunctionDeployment.setHandler(handlerPath);
        dbFunctionDeployment.setStatus(DeployStatus.CREATED);
        dbFunctionDeployment.setTimeoutSecs(random ? (int) (Math.random() * 10) + 3 : 3);

        dbFunctionDeployment.setFunctionImplementation(dbFunctionImplementation);

        dbFunctionDeployment.setFunction(function);

        return dbFunctionDeployment;

    }

}
