package com.asi.hms.service;

import com.asi.hms.model.api.*;
import com.asi.hms.model.db.DBFunctionDeployment;
import com.asi.hms.model.db.DBFunctionImplementation;
import com.asi.hms.model.db.DBFunctionType;
import com.asi.hms.repository.FunctionTypeRepository;
import com.asi.hms.utils.cloudproviderutils.YamlParser;
import com.asi.hms.utils.cloudproviderutils.migrate.MigrationRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FunctionService {

    private final FunctionDeploymentService functionDeploymentService;
    private final FunctionImplementationService functionImplementationService;
    private final FunctionTypeService functionTypeService;
    private final FunctionTypeRepository functionTypeRepository;
    private final UserService userService;
    private final UploadFileService uploadFileService;

    public FunctionService(FunctionDeploymentService functionDeploymentService,
                           FunctionImplementationService functionImplementationService,
                           FunctionTypeService functionTypeService,
                           FunctionTypeRepository functionTypeRepository,
                           UserService userService,
                           UploadFileService uploadFileService) {

        this.functionDeploymentService = functionDeploymentService;
        this.functionImplementationService = functionImplementationService;
        this.functionTypeService = functionTypeService;

        this.functionTypeRepository = functionTypeRepository;
        this.userService = userService;
        this.uploadFileService = uploadFileService;

    }

    public List<APIFunction> getAllFunctions() {

        List<DBFunctionType> functionTypes = this.functionTypeRepository.findAll().stream().toList();

        List<APIFunction> apiFunctions = new ArrayList<>();

        for (DBFunctionType functionType : functionTypes) {

            if (functionType.getFunctionImplementations() == null || functionType.getFunctionImplementations().isEmpty()) {

                APIFunction apiFunction = new APIFunction();
                apiFunction.setFunctionType(APIFunctionType.fromDBFunctionType(functionType));
                apiFunction.setFunctionImplementation(null);
                apiFunction.setFunctionDeployment(null);
                apiFunctions.add(apiFunction);


            }

            for (DBFunctionImplementation functionImplementation : functionType.getFunctionImplementations()) {

                if (functionImplementation.getFunctionDeployments() == null || functionImplementation.getFunctionDeployments().isEmpty()) {

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

        List<APIUser> users = this.userService.getAllUser();

        return MigrationRunner
                .getMigrationRunner(apiMigrationPreparation.getMigrationType(), users)
                .prepareMigration(apiMigrationPreparation);

    }

    public APIMigration migrate(APIMigration apiMigration) {

        // TODO: also other migrations needed? currently just functionDeployment is migrated!

        apiMigration.getFunctions()
                .stream()
                .map(APIFunction::getFunctionDeployment)
                .forEach(this.functionDeploymentService::add);

        return apiMigration;

    }

    public String getYaml(List<APIFunction> functions) {
        return YamlParser.writeYaml(functions);
    }

    public List<APIFunctionType> uploadYaml(MultipartFile file, UUID userID) {

        // TODO: probably only a temp file needed?

        Path path = this.uploadFileService.uploadFile(file, UploadFileService.UPLOADS_DIR);

        List<APIFunctionType> apiFunctions = YamlParser.readYamlAsTree(path);

        this.addAll(apiFunctions, userID);

        return apiFunctions;

    }

    private void addAll(List<APIFunctionType> apiFunctionTypes, UUID userID) {

        for (APIFunctionType apiFunctionType : apiFunctionTypes) {

            APIFunctionType addedFunctionType = this.functionTypeService.add(apiFunctionType);

            for (APIFunctionImplementation apiFunctionImplementation : apiFunctionType.getFunctionImplementations()) {

                apiFunctionImplementation.setFunctionTypeId(addedFunctionType.getId());
                APIFunctionImplementation addedFunctionImplementation = this.functionImplementationService.add(
                        null,
                        apiFunctionImplementation
                );

                for (APIFunctionDeployment apiFunctionDeployment : apiFunctionImplementation.getFunctionDeployments()) {

                    apiFunctionDeployment.setFunctionImplementationId(addedFunctionImplementation.getId());
                    apiFunctionDeployment.setUserId(userID);
                    this.functionDeploymentService.add(apiFunctionDeployment);

                }

            }

        }

    }

    public List<APIFunction> uploadPackage(MultipartFile file) {

        this.uploadFileService.uploadZipFileAndNormalize(file, UploadFileService.UPLOADS_DIR);

        return List.of();


//        return YamlParser.readYaml(yamlString);


    }
}
