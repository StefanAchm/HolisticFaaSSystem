package com.asi.hms.service;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.APIFunctionDeployment;
import com.asi.hms.model.api.APIWorkflowDeployment;
import com.asi.hms.model.db.*;
import com.asi.hms.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WorkflowDeploymentService {

    private final WorkflowDeploymentRepository workflowDeploymentRepository;
    private final WorkflowRepository workflowRepository;
    private final UserRepository userRepository;
    private final FunctionDeploymentRepository functionDeploymentRepository;
    private final FunctionImplementationRepository functionImplementationRepository;

    public WorkflowDeploymentService(WorkflowDeploymentRepository workflowDeploymentRepository,
                                     WorkflowRepository workflowRepository,
                                     UserRepository userRepository,
                                     FunctionDeploymentRepository functionDeploymentRepository,
                                     FunctionImplementationRepository functionImplementationRepository) {

        this.workflowDeploymentRepository = workflowDeploymentRepository;
        this.workflowRepository = workflowRepository;
        this.userRepository = userRepository;
        this.functionDeploymentRepository = functionDeploymentRepository;
        this.functionImplementationRepository = functionImplementationRepository;

    }

    public void add(APIWorkflowDeployment apiWorkflowDeployment) {

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

        this.workflowDeploymentRepository.save(dbWorkflowDeployment);

        for(APIFunctionDeployment functionDeployment : apiWorkflowDeployment.getFunctionDeployments()) {

            DBFunctionImplementation dbFunctionImplementation =
                    this.functionImplementationRepository.findById(functionDeployment.getFunctionImplementationId()).orElseThrow(
                            () -> new HolisticFaaSException("Function implementation not found")
                    );

            DBFunctionDeployment dbFunctionDeployment = DBFunctionDeployment.fromAPIFunctionDeployment(
                    functionDeployment,
                    user,
                    dbFunctionImplementation
            );

            dbFunctionDeployment.setWorkflowDeployment(dbWorkflowDeployment);
            this.functionDeploymentRepository.save(dbFunctionDeployment);

        }

    }

    public APIWorkflowDeployment getWorkflowDeployment(UUID id) {
        return this.workflowDeploymentRepository.findById(id).map(APIWorkflowDeployment::fromDBWorkflowDeployment).orElseThrow(
                () -> new HolisticFaaSException("Workflow deployment not found")
        );
    }

    public List<APIWorkflowDeployment> getAllWorkflowDeployments() {
        return this.workflowDeploymentRepository.findAll().stream().map(APIWorkflowDeployment::fromDBWorkflowDeployment).toList();
    }
}
