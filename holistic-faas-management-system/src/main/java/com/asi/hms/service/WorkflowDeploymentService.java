package com.asi.hms.service;

import com.asi.hms.enums.MigrationType;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.*;
import com.asi.hms.model.db.*;
import com.asi.hms.repository.*;
import com.asi.hms.utils.FileUtil;
import com.asi.hms.utils.cloudproviderutils.YamlParser;
import com.asi.hms.utils.cloudproviderutils.afcl.AfclParser;
import com.asi.hms.utils.cloudproviderutils.migrate.MigrationRunner;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WorkflowDeploymentService {

    private final WorkflowDeploymentRepository workflowDeploymentRepository;
    private final WorkflowRepository workflowRepository;

    private final UserRepository userRepository;

    private final FunctionDeploymentRepository functionDeploymentRepository;
    private final FunctionImplementationRepository functionImplementationRepository;
    private final FunctionRepository functionRepository;

    private final UserService userService;
    private final FunctionDeploymentService functionDeploymentService;

    public WorkflowDeploymentService(WorkflowDeploymentRepository workflowDeploymentRepository,
                                     WorkflowRepository workflowRepository,
                                     UserRepository userRepository,
                                     FunctionDeploymentRepository functionDeploymentRepository,
                                     FunctionImplementationRepository functionImplementationRepository,
                                     FunctionRepository functionRepository,
                                     UserService userService,
                                     FunctionDeploymentService functionDeploymentService
    ) {

        this.workflowDeploymentRepository = workflowDeploymentRepository;
        this.workflowRepository = workflowRepository;

        this.userRepository = userRepository;

        this.functionDeploymentRepository = functionDeploymentRepository;
        this.functionImplementationRepository = functionImplementationRepository;
        this.functionRepository = functionRepository;

        this.userService = userService;
        this.functionDeploymentService = functionDeploymentService;

    }

    public APIWorkflowDeployment add(APIWorkflowDeployment apiWorkflowDeployment) {

        DBWorkflow dbWorkflow = this.workflowRepository.findById(apiWorkflowDeployment.getWorkflow().getId()).orElseThrow(
                () -> new HolisticFaaSException("Workflow not found")
        );

        DBUser user = this.userRepository
                .findById(apiWorkflowDeployment.getUser().getId())
                .orElseThrow(() -> new HolisticFaaSException("User '" + apiWorkflowDeployment.getUser().getId() + "'not found"));

        DBWorkflowDeployment dbWorkflowDeployment = new DBWorkflowDeployment();
        dbWorkflowDeployment.setWorkflow(dbWorkflow);
        dbWorkflowDeployment.setUser(user);
        dbWorkflowDeployment.setName(apiWorkflowDeployment.getName());
        dbWorkflowDeployment.setFunctionDeployments(new ArrayList<>());

        this.workflowDeploymentRepository.save(dbWorkflowDeployment);

        for (APIFunctionFlat functionDefinition : apiWorkflowDeployment.getFunctionDefinitions()) {

            DBFunctionImplementation dbFunctionImplementation =
                    this.functionImplementationRepository.findById(functionDefinition.getFunctionImplementation().getId()).orElseThrow(
                            () -> new HolisticFaaSException("Function implementation not found")
                    );

            DBFunction dbFunction = this.functionRepository.findById(functionDefinition.getFunction().getId()).orElseThrow(
                    () -> new HolisticFaaSException("Function not found")
            );

            DBFunctionDeployment dbFunctionDeployment = DBFunctionDeployment.fromAPIFunctionDeployment(
                    functionDefinition.getFunctionDeployment(),
                    user,
                    dbFunctionImplementation,
                    dbFunction
            );

            dbFunctionDeployment.setWorkflowDeployment(dbWorkflowDeployment);
            this.functionDeploymentRepository.save(dbFunctionDeployment);

            dbWorkflowDeployment.getFunctionDeployments().add(dbFunctionDeployment);

        }

        return APIWorkflowDeployment.fromDBWorkflowDeployment(dbWorkflowDeployment);

    }

    public APIWorkflowDeployment getWorkflowDeployment(UUID id) {
        return this.workflowDeploymentRepository.findById(id).map(APIWorkflowDeployment::fromDBWorkflowDeployment).orElseThrow(
                () -> new HolisticFaaSException("Workflow deployment not found")
        );
    }

    public List<APIWorkflowDeployment> getAllWorkflowDeployments() {
        return this.workflowDeploymentRepository.findAll().stream().map(APIWorkflowDeployment::fromDBWorkflowDeployment).toList();
    }

    public List<APIFunctionFlat> getFunctionDeployments(UUID workflowDeploymentId) {

        List<DBFunctionDeployment> allByWorkflowDeploymentId = this.functionDeploymentRepository.findAllByWorkflowDeploymentId(workflowDeploymentId);

        List<APIFunctionFlat> apiFunctionFlats = new ArrayList<>();

        for (DBFunctionDeployment dbFunctionDeployment : allByWorkflowDeploymentId) {

            APIFunctionFlat apiFunctionFlat = new APIFunctionFlat();

            apiFunctionFlat.setFunctionDeployment(APIFunctionDeployment.fromDBFunctionDeployment(dbFunctionDeployment));
            apiFunctionFlat.setFunctionImplementation(APIFunctionImplementation.fromDBFunctionImplementation(dbFunctionDeployment.getFunctionImplementation()));
            apiFunctionFlat.setFunctionType(APIFunctionType.fromDBFunctionType(dbFunctionDeployment.getFunctionImplementation().getFunctionType()));

            apiFunctionFlats.add(apiFunctionFlat);

        }

        return apiFunctionFlats;

    }

    public APIWorkflowDeploymentMigrationResponse migrate(APIWorkflowDeploymentMigration apiWorkflowDeploymentMigration) {

        List<APIUser> users = this.userService.getAllUser();

        APIMigrationPreparation apiMigrationPreparation = new APIMigrationPreparation();
        apiMigrationPreparation.setMigrationType(apiWorkflowDeploymentMigration.getMigrationType());
        apiMigrationPreparation.setTarget(apiWorkflowDeploymentMigration.getTarget());
        apiMigrationPreparation.setFunctions(apiWorkflowDeploymentMigration.getWorkflowDeployment().getFunctionDefinitions());

        APIMigration apiMigration = MigrationRunner
                .getMigrationRunner(apiWorkflowDeploymentMigration.getMigrationType(), users)
                .prepareMigration(apiMigrationPreparation);

        APIWorkflowDeployment apiWorkflowDeployment = new APIWorkflowDeployment();
        apiWorkflowDeployment.setWorkflow(apiWorkflowDeploymentMigration.getWorkflowDeployment().getWorkflow());
        apiWorkflowDeployment.setUser(apiWorkflowDeploymentMigration.getWorkflowDeployment().getUser());
        apiWorkflowDeployment.setName(apiWorkflowDeploymentMigration.getWorkflowDeployment().getName());
        apiWorkflowDeployment.setFunctionDefinitions(apiMigration.getFunctions());

        if (apiMigration.isValid() && apiWorkflowDeploymentMigration.getMigrationType().equals(MigrationType.FUNCTION_USER)) {
            apiWorkflowDeployment = this.add(apiWorkflowDeployment);
        }

        APIWorkflowDeploymentMigrationResponse apiWorkflowDeploymentMigrationResponse = new APIWorkflowDeploymentMigrationResponse(apiWorkflowDeployment);
        apiWorkflowDeploymentMigrationResponse.setUserMigrations(apiMigration.getUserMigrations());
        apiWorkflowDeploymentMigrationResponse.setRegionMigrations(apiMigration.getRegionMigrations());
        apiWorkflowDeploymentMigrationResponse.setRuntimeMigrations(apiMigration.getRuntimeMigrations());

        return apiWorkflowDeploymentMigrationResponse;

    }

    public void download(UUID workflowDeploymentId, OutputStream outputStream) throws IOException {

        DBWorkflowDeployment dbWorkflowDeployment = this.workflowDeploymentRepository.findById(workflowDeploymentId).orElseThrow(
                () -> new HolisticFaaSException("Workflow deployment not found")
        );

        // 1. Create a tmp folder
        Path workflowDeployment = Files.createTempDirectory("workflowDeployment");

        // 2. Copy the workflow file into the tmp folder
        String workflowFilePathAsString = dbWorkflowDeployment.getWorkflow().getFilePath();
        Path workflowFilePath = Path.of(workflowFilePathAsString);
        Files.copy(workflowFilePath, workflowDeployment.resolve(workflowFilePath.getFileName()));

        // 3. Copy the functions.yaml file into the tmp folder
        List<APIFunctionFlat> apiFunctionFlats = this.getFunctionDeployments(workflowDeploymentId);
        String functionsYamlContent = YamlParser.writeYaml(apiFunctionFlats);
        Files.write(workflowDeployment.resolve("functions.yaml"), functionsYamlContent.getBytes());

        // 4. Copy all function implementations into the tmp folder
        Path functionImplementationsPath = Files.createDirectory(workflowDeployment.resolve("functionImplementations"));
        for(DBFunctionDeployment dbFunctionDeployment : dbWorkflowDeployment.getFunctionDeployments()) {

            DBFunctionImplementation dbFunctionImplementation = dbFunctionDeployment.getFunctionImplementation();

            String functionImplementationFilePathAsString = dbFunctionImplementation.getFilePath();
            Path functionImplementationFilePath = Path.of(functionImplementationFilePathAsString);

            Path functionPath = Files.createDirectory(functionImplementationsPath.resolve(dbFunctionDeployment.getFunction().getName()));
            Files.copy(functionImplementationFilePath, functionPath.resolve(functionImplementationFilePath.getFileName()));

        }

        Path zipOutFile = Files.createTempFile("workflowDeployment", ".zip");
        FileUtil.zipFolderV2(workflowDeployment, zipOutFile);

//        return Files.readAllBytes(zipOutFile); // TODO: delete tmp folder

        // 5. Copy the zip file into the output stream
        Files.copy(zipOutFile, outputStream);

    }

    public byte[] downloadYaml(UUID workflowDeploymentId) throws IOException {

        DBWorkflowDeployment dbWorkflowDeployment = this.workflowDeploymentRepository.findById(workflowDeploymentId).orElseThrow(
                () -> new HolisticFaaSException("Workflow deployment not found")
        );

        String workflowFilePathAsString = dbWorkflowDeployment.getWorkflow().getFilePath();

        String outfileName = "workflow_" + dbWorkflowDeployment.getId() +" .yaml";

        AfclParser.createWorkflowAtPath(
                Path.of(workflowFilePathAsString).toFile(),
                APIWorkflowDeployment.fromDBWorkflowDeployment(dbWorkflowDeployment),
                outfileName
        );

        byte[] bytes = Files.readAllBytes(Path.of(outfileName));

        // Delete the file after reading
        File file = new File(outfileName);
        file.delete();

        return bytes;

    }

}
