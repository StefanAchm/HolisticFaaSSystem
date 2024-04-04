package com.asi.hms.service;

import com.asi.hms.model.api.APIFunction;
import com.asi.hms.model.api.APIFunctionDeployment;
import com.asi.hms.model.api.APIFunctionImplementation;
import com.asi.hms.model.api.APIFunctionType;
import com.asi.hms.model.db.DBFunctionDeployment;
import com.asi.hms.model.db.DBFunctionImplementation;
import com.asi.hms.model.db.DBFunctionType;
import com.asi.hms.repository.FunctionDeploymentRepository;
import com.asi.hms.repository.FunctionTypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FunctionService {

    private final FunctionTypeRepository functionTypeRepository;
    private final FunctionDeploymentRepository functionDeploymentRepository;

    public FunctionService(FunctionDeploymentRepository functionDeploymentRepository,
                           FunctionTypeRepository functionTypeRepository
    ) {

        this.functionDeploymentRepository = functionDeploymentRepository;
        this.functionTypeRepository = functionTypeRepository;

    }

    public List<APIFunction> getAllFunctions() {

        List<DBFunctionType> functionTypes = this.functionTypeRepository.findAll().stream().toList();

        List<APIFunction> apiFunctions = new ArrayList<>();

        for (DBFunctionType functionType : functionTypes) {

            if(functionType.getFunctionImplementations() == null || functionType.getFunctionImplementations().isEmpty()) {

                APIFunction apiFunction = new APIFunction();
                apiFunction.setFunctionType(APIFunctionType.fromDBFunctionType(functionType));
                apiFunction.setFunctionImplementation(null);
                apiFunction.setFunctionDeployment(null);
                apiFunctions.add(apiFunction);


            }

            for (DBFunctionImplementation functionImplementation : functionType.getFunctionImplementations()) {

                if(functionImplementation.getFunctionDeployments() == null || functionImplementation.getFunctionDeployments().isEmpty()) {

                    APIFunction apiFunction = new APIFunction();
                    apiFunction.setFunctionType(APIFunctionType.fromDBFunctionType(functionType));
                    apiFunction.setFunctionImplementation(APIFunctionImplementation.fromDBFunctionImplementation(functionImplementation));
                    apiFunction.setFunctionDeployment(null);
                    apiFunctions.add(apiFunction);

                }


                for (DBFunctionDeployment functionDeployment : functionImplementation.getFunctionDeployments()) {

                    APIFunction apiFunction = new APIFunction();

                    apiFunction.setFunctionType(APIFunctionType.fromDBFunctionType(functionType));
                    apiFunction.setFunctionImplementation(APIFunctionImplementation.fromDBFunctionImplementation(functionImplementation));
                    apiFunction.setFunctionDeployment(APIFunctionDeployment.fromDBFunctionDeployment(functionDeployment));

                    apiFunctions.add(apiFunction);


                }

            }

        }

        return apiFunctions;

    }

}
