package com.asi.hms.service;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.APIFunction;
import com.asi.hms.model.db.DBFunction;
import com.asi.hms.model.db.DBFunctionType;
import com.asi.hms.model.db.DBWorkflow;
import com.asi.hms.repository.FunctionRepository;
import com.asi.hms.repository.FunctionTypeRepository;
import com.asi.hms.repository.WorkflowRepository;
import org.springframework.stereotype.Service;

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

        DBFunctionType dbFunctionType = this.functionTypeRepository.save(
                DBFunctionType.fromAPIFunctionType(apiFunction.getFunctionType())
        );

        DBFunction dbFunction = DBFunction.fromAPIFunction(apiFunction);
        dbFunction.setWorkflow(dbWorkflow);
        dbFunction.setFunctionType(dbFunctionType);

        return APIFunction.fromDBFunction(this.functionRepository.save(dbFunction));

    }

}

