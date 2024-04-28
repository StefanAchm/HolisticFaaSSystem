package com.asi.hms.service;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.*;
import com.asi.hms.model.db.*;
import com.asi.hms.repository.*;
import com.asi.hms.utils.cloudproviderutils.migrate.MigrationRunner;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

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

    public APIWorkflowDeployment migrate(APIWorkflowDeploymentMigration apiWorkflowDeploymentMigration) {

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

        if(apiMigration.isValid()) {
            apiWorkflowDeployment = this.add(apiWorkflowDeployment);
        }

        apiWorkflowDeployment.setValid(apiMigration.isValid());

        return apiWorkflowDeployment;

    }

}
