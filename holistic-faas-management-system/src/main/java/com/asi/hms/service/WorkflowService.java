package com.asi.hms.service;

import com.asi.hms.model.api.*;
import com.asi.hms.model.db.DBFunction;
import com.asi.hms.model.db.DBFunctionType;
import com.asi.hms.model.db.DBWorkflow;
import com.asi.hms.repository.FunctionImplementationRepository;
import com.asi.hms.repository.FunctionRepository;
import com.asi.hms.repository.FunctionTypeRepository;
import com.asi.hms.repository.WorkflowRepository;
import com.asi.hms.utils.cloudproviderutils.afcl.AfclParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class WorkflowService {

    private final WorkflowRepository workflowRepository;

    private final FunctionRepository functionRepository;
    private final FunctionTypeRepository functionTypeRepository;
    private final FunctionImplementationRepository functionImplementationRepository;

    public WorkflowService(WorkflowRepository workflowRepository,
                           FunctionRepository functionRepository,
                           FunctionTypeRepository functionTypeRepository,
                           FunctionImplementationRepository functionImplementationRepository) {

        this.workflowRepository = workflowRepository;

        this.functionRepository = functionRepository;
        this.functionTypeRepository = functionTypeRepository;
        this.functionImplementationRepository = functionImplementationRepository;
    }

    public List<APIWorkflow> getAllWorkflows() {
        return this.workflowRepository.findAll().stream().map(APIWorkflow::fromDBWorkflow).toList();
    }


    public APIWorkflow getWorkflow(UUID id) {
        return this.workflowRepository.findById(id).map(APIWorkflow::fromDBWorkflow).orElse(null);
    }

    public APIWorkflow add(MultipartFile file) {

        APIWorkflow workflow = AfclParser.getWorkflow(file);

        DBWorkflow dbWorkflow = DBWorkflow.fromAPIWorkflow(workflow);
        this.workflowRepository.save(dbWorkflow);

        for (APIFunction2 apiFunction : workflow.getFunctions()) {

            DBFunctionType dbFunctionType = DBFunctionType.fromAPIFunctionType(apiFunction.getFunctionType());
            this.functionTypeRepository.save(dbFunctionType);

            DBFunction dbFunction = DBFunction.fromAPIFunction(apiFunction);
            dbFunction.setWorkflow(dbWorkflow);
            dbFunction.setFunctionType(dbFunctionType);
            this.functionRepository.save(dbFunction);

        }

        return workflow;

    }

    public List<APIWorkflowDeployment> getDeployments(UUID id) {

        DBWorkflow dbWorkflow = this.workflowRepository.findById(id)
                .orElseThrow(
                () -> new RuntimeException("Workflow not found")
        );

        return dbWorkflow.getWorkflowDeployments()
                .stream()
                .map(APIWorkflowDeployment::fromDBWorkflowDeployment)
                .toList();

    }

//    public List<APIFunctionImplementation> getImplementations(UUID id) {
//
//        return this.functionImplementationRepository.findByFunctionWorkflowId(id)
//                .stream()
//                .map(APIFunctionImplementation::fromDBFunctionImplementation)
//                .toList();
//
//    }

    public List<APIFunctionImplementation> getWorkflowFunctionImplementations(UUID workflowId) {
        return this.functionImplementationRepository.findByFunctionWorkflowId(workflowId)
                .stream()
                .map(APIFunctionImplementation::fromDBFunctionImplementation)
                .toList();
    }

    public List<APIFunctionType> getWorkflowFunctionTypes(UUID workflowId) {
        return this.functionTypeRepository.findByFunctionWorkflowId(workflowId)
                .stream()
                .map(APIFunctionType::fromDBFunctionType)
                .toList();
    }
}
