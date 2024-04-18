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
import springfox.documentation.spring.web.plugins.Docket;

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
    private final Docket api;

    public FunctionService(FunctionDeploymentService functionDeploymentService,
                           FunctionImplementationService functionImplementationService,
                           FunctionTypeService functionTypeService,
                           FunctionTypeRepository functionTypeRepository,
                           UserService userService,
                           UploadFileService uploadFileService,
                           Docket api) {

        this.functionDeploymentService = functionDeploymentService;
        this.functionImplementationService = functionImplementationService;
        this.functionTypeService = functionTypeService;

        this.functionTypeRepository = functionTypeRepository;
        this.userService = userService;
        this.uploadFileService = uploadFileService;
        this.api = api;
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

    public void addAll(List<APIFunction> apiFunctions) {

        for (APIFunction apiFunction : apiFunctions) {

            // TODO: handle duplicates!?

            APIFunctionType apiFunctionType = this.functionTypeService.add(apiFunction.getFunctionType());
            apiFunction.getFunctionImplementation().setFunctionTypeId(apiFunctionType.getId());
            APIFunctionImplementation apiFunctionImplementation = this.functionImplementationService.add(null, apiFunction.getFunctionImplementation());
            apiFunction.getFunctionDeployment().setFunctionImplementationId(apiFunctionImplementation.getId());
            this.functionDeploymentService.add(apiFunction.getFunctionDeployment());

        }

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

    public List<APIFunction> uploadYaml(MultipartFile file, UUID userID) {

        // TODO: probably only a temp file needed?

        Path path = this.uploadFileService.uploadFile(file, UploadFileService.UPLOADS_DIR);

        List<APIFunction> apiFunctions = YamlParser.readYaml(path);

        apiFunctions.forEach(apiFunction -> apiFunction.getFunctionDeployment().setUserId(userID));

        this.addAll(apiFunctions);

        return apiFunctions;

    }

    public List<APIFunction> uploadPackage(MultipartFile file) {

        this.uploadFileService.uploadZipFileAndNormalize(file, UploadFileService.UPLOADS_DIR);

        return List.of();


//        return YamlParser.readYaml(yamlString);


    }
}
