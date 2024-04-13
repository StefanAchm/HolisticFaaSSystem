package com.asi.hms.service;

import com.asi.hms.model.api.*;
import com.asi.hms.model.db.DBFunctionDeployment;
import com.asi.hms.model.db.DBFunctionImplementation;
import com.asi.hms.model.db.DBFunctionType;
import com.asi.hms.repository.FunctionTypeRepository;
import com.asi.hms.utils.cloudproviderutils.migrate.MigrationRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FunctionService {

    private final FunctionDeploymentService functionDeploymentService;
    private final FunctionTypeRepository functionTypeRepository;

    public FunctionService(FunctionDeploymentService functionDeploymentService,
                           FunctionTypeRepository functionTypeRepository) {

        this.functionDeploymentService = functionDeploymentService;
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

    public APIMigration prepareMigration(APIMigrationPreparation apiMigrationPreparation) {

        return MigrationRunner
                .getMigrationRunner(apiMigrationPreparation.getMigrationType())
                .prepareMigration(apiMigrationPreparation);

    }

    public APIMigration migrate(APIMigration apiMigration) {

        apiMigration.getFunctions()
                .stream()
                .map(APIFunction::getFunctionDeployment)
                .forEach(this.functionDeploymentService::addFunctionDeployment);

        return apiMigration;

    }

}
