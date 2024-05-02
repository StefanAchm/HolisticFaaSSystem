package com.asi.hms.service;

import com.asi.hms.exceptions.HolisticFaaSException;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WorkflowService {

    private final WorkflowRepository workflowRepository;

    private final FunctionRepository functionRepository;
    private final FunctionTypeRepository functionTypeRepository;
    private final FunctionImplementationRepository functionImplementationRepository;

    private final UploadFileService uploadFileService;

    public WorkflowService(WorkflowRepository workflowRepository,
                           FunctionRepository functionRepository,
                           FunctionTypeRepository functionTypeRepository,
                           FunctionImplementationRepository functionImplementationRepository,
                           UploadFileService uploadFileService) {

        this.workflowRepository = workflowRepository;

        this.functionRepository = functionRepository;
        this.functionTypeRepository = functionTypeRepository;
        this.functionImplementationRepository = functionImplementationRepository;

        this.uploadFileService = uploadFileService;

    }

    public List<APIWorkflow> getAllWorkflows() {
        return this.workflowRepository.findAll().stream().map(APIWorkflow::fromDBWorkflow).toList();
    }


    public APIWorkflow getWorkflow(UUID id) {
        return this.workflowRepository.findById(id).map(APIWorkflow::fromDBWorkflow).orElse(null);
    }

    public APIWorkflow add(MultipartFile file, APIWorkflow workflowFromRequest) {

        APIWorkflow workflow = new APIWorkflow();

        if(file != null) {

            String path = this.uploadFileService.uploadFileAndNormalize(file, UploadFileService.FUNCTIONS_DIR);

            APIWorkflow workflowFromFile = AfclParser.getWorkflow(file);
            workflow.setName(workflowFromFile.getName());
            workflow.setFunctions(workflowFromFile.getFunctions());

            workflow.setFilePath(path);

        }

        if(workflowFromRequest != null) {
            workflow.setName(workflowFromRequest.getName());
            workflow.setDescription(workflowFromRequest.getDescription());
        }

        DBWorkflow dbWorkflow = DBWorkflow.fromAPIWorkflow(workflow);
        this.workflowRepository.save(dbWorkflow);

        for (APIFunction apiFunction : workflow.getFunctions()) {

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

    public APIWorkflowDeployment prepareWorkflowDeployment(UUID workflowId) {

        APIWorkflowDeployment apiWorkflowDeployment = new APIWorkflowDeployment();
        apiWorkflowDeployment.setFunctionDefinitions(new ArrayList<>());

        DBWorkflow dbWorkflow = this.workflowRepository.findById(workflowId)
                .orElseThrow(
                () -> new RuntimeException("Workflow not found")
        );

        apiWorkflowDeployment.setWorkflow(APIWorkflow.fromDBWorkflow(dbWorkflow));

        for(DBFunction dbFunction : dbWorkflow.getFunctions()) {

            APIFunctionFlat apiFunctionDefinition = new APIFunctionFlat();

            apiFunctionDefinition.setFunction(APIFunction.fromDBFunction(dbFunction));

            apiFunctionDefinition.setFunctionType(APIFunctionType.fromDBFunctionType(dbFunction.getFunctionType()));

            apiFunctionDefinition.setFunctionImplementation(new APIFunctionImplementation());

            apiFunctionDefinition.setFunctionDeployment(new APIFunctionDeployment());

            apiWorkflowDeployment.getFunctionDefinitions().add(apiFunctionDefinition);

        }

        return apiWorkflowDeployment;

    }

    public APIWorkflow add(APIWorkflow workflow) {

        DBWorkflow dbWorkflow = this.workflowRepository.save(DBWorkflow.fromAPIWorkflow(workflow));

        return APIWorkflow.fromDBWorkflow(dbWorkflow);

    }

    public byte[] downloadAbstractWorkflow(UUID workflowId) {

        DBWorkflow workflow = this.workflowRepository.findById(workflowId).orElseThrow(
                () -> new HolisticFaaSException("Workflow not found")
        );

        try {

            String filePath = workflow.getFilePath();

            File file = new File(filePath);

            return Files.readAllBytes(file.toPath());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
