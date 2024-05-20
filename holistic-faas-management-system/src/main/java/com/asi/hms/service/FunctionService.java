package com.asi.hms.service;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.APIFunction;
import com.asi.hms.model.db.*;
import com.asi.hms.repository.FunctionRepository;
import com.asi.hms.repository.FunctionTypeRepository;
import com.asi.hms.repository.WorkflowRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FunctionService {

    private final WorkflowRepository workflowRepository;

    private final FunctionRepository functionRepository;
    private final FunctionTypeRepository functionTypeRepository;

    public FunctionService(WorkflowRepository workflowRepository,
                           FunctionRepository functionRepository,
                           FunctionTypeRepository functionTypeRepository) {

        this.workflowRepository = workflowRepository;

        this.functionRepository = functionRepository;
        this.functionTypeRepository = functionTypeRepository;

    }


    public APIFunction add(APIFunction apiFunction) {

        DBWorkflow dbWorkflow = this.workflowRepository.findById(apiFunction.getWorkflowId())
                .orElseThrow(() -> new HolisticFaaSException("Workflow not found"));

        List<DBFunctionType> dbFunctionTypes = this.functionTypeRepository.findByFunctionWorkflowIdAndName(
                dbWorkflow.getId(), apiFunction.getFunctionType().getName());

        DBFunctionType dbFunctionType = dbFunctionTypes.isEmpty()
                ? this.functionTypeRepository.save(DBFunctionType.fromAPIFunctionType(apiFunction.getFunctionType()))
                : dbFunctionTypes.get(0);

        DBFunction dbFunction = DBFunction.fromAPIFunction(apiFunction);
        dbFunction.setWorkflow(dbWorkflow);
        dbFunction.setFunctionType(dbFunctionType);

        return APIFunction.fromDBFunction(this.functionRepository.save(dbFunction));

    }

    public boolean validateUser(UUID workflowId) {

        DBWorkflow workflow = this.workflowRepository
                .findById(workflowId)
                .orElseThrow(() -> new HolisticFaaSException("Workflow not found"));


        DBUser dbUser = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getDbUser();

        return dbUser.getId().equals(workflow.getCreatedBy().getId());

    }

}

