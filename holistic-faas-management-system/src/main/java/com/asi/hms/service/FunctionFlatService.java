package com.asi.hms.service;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.PackageImport;
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
public class FunctionFlatService {

    private final FunctionDeploymentService functionDeploymentService;
    private final FunctionImplementationService functionImplementationService;
    private final FunctionTypeService functionTypeService;
    private final FunctionTypeRepository functionTypeRepository;
    private final UserService userService;
    private final UploadFileService uploadFileService;

    public FunctionFlatService(FunctionDeploymentService functionDeploymentService,
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

    public List<APIFunctionFlat> getAllFunctions() {

        List<DBFunctionType> functionTypes = this.functionTypeRepository.findAll().stream().toList();

        List<APIFunctionFlat> apiFunctionFlats = new ArrayList<>();

        for (DBFunctionType functionType : functionTypes) {

            if (functionType.getFunctionImplementations() == null || functionType.getFunctionImplementations().isEmpty()) {

                APIFunctionFlat apiFunctionFlat = new APIFunctionFlat();
                apiFunctionFlat.setFunctionType(APIFunctionType.fromDBFunctionType(functionType));
                apiFunctionFlat.setFunctionImplementation(null);
                apiFunctionFlat.setFunctionDeployment(null);
                apiFunctionFlats.add(apiFunctionFlat);

            }

            for (DBFunctionImplementation functionImplementation : functionType.getFunctionImplementations()) {

                if (functionImplementation.getFunctionDeployments() == null || functionImplementation.getFunctionDeployments().isEmpty()) {

                    APIFunctionFlat apiFunctionFlat = new APIFunctionFlat();
                    apiFunctionFlat.setFunctionType(APIFunctionType.fromDBFunctionType(functionType));
                    apiFunctionFlat.setFunctionImplementation(APIFunctionImplementation.fromDBFunctionImplementation(functionImplementation));
                    apiFunctionFlat.setFunctionDeployment(null);
                    apiFunctionFlats.add(apiFunctionFlat);

                }


                for (DBFunctionDeployment functionDeployment : functionImplementation.getFunctionDeployments()) {

                    APIFunctionFlat apiFunctionFlat = new APIFunctionFlat();

                    apiFunctionFlat.setFunctionType(APIFunctionType.fromDBFunctionType(functionType));
                    apiFunctionFlat.setFunctionImplementation(APIFunctionImplementation.fromDBFunctionImplementation(functionImplementation));
                    apiFunctionFlat.setFunctionDeployment(APIFunctionDeployment.fromDBFunctionDeployment(functionDeployment));

                    apiFunctionFlats.add(apiFunctionFlat);


                }

            }

        }

        return apiFunctionFlats;

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
                .map(APIFunctionFlat::getFunctionDeployment)
                .forEach(this.functionDeploymentService::add);

        return apiMigration;

    }

    public String getYaml(List<APIFunctionFlat> functions) {
        return YamlParser.writeYaml(functions);
    }

    public List<APIFunctionType> uploadYaml(MultipartFile file, UUID userID) {

        // TODO: probably only a temp file needed?

        Path path = this.uploadFileService.uploadFile(file, UploadFileService.UPLOADS_DIR);

        List<APIFunctionType> apiFunctions = YamlParser.readYamlAsTree(path);

        this.addAll(apiFunctions, userID);

        return apiFunctions;

    }


    public List<APIFunctionType> uploadPackage(MultipartFile file, UUID userId) {

        Path path;

        try {
            path = this.uploadFileService.uploadZipFile(file, UploadFileService.UPLOADS_DIR);
        } catch (Exception e) {
            throw new HolisticFaaSException("Can not upload package");
        }

        PackageImport packageImport = PackageImport.getPackageImport(path);

        this.addAll(packageImport.getApiFunctionTypes(), userId);

        return packageImport.getApiFunctionTypes();

    }

    private void addAll(List<APIFunctionType> apiFunctionTypes, UUID userID) {

        for (APIFunctionType apiFunctionType : apiFunctionTypes) {

            APIFunctionType addedFunctionType = this.functionTypeService.add(apiFunctionType);

            for (APIFunctionImplementation apiFunctionImplementation : apiFunctionType.getFunctionImplementations()) {

                apiFunctionImplementation.setFunctionTypeId(addedFunctionType.getId());
                APIFunctionImplementation addedFunctionImplementation = this.functionImplementationService.add(apiFunctionImplementation);

                for (APIFunctionDeployment apiFunctionDeployment : apiFunctionImplementation.getFunctionDeployments()) {

                    apiFunctionDeployment.setFunctionImplementationId(addedFunctionImplementation.getId());
                    apiFunctionDeployment.setUserId(userID);
                    this.functionDeploymentService.add(apiFunctionDeployment);

                }

            }

        }

    }

    public List<APIFunctionType> upload(MultipartFile file, UUID userId) {

        if(file == null || file.isEmpty()) {
            throw new HolisticFaaSException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();

        if(originalFilename == null) {
            throw new HolisticFaaSException("File name is null");
        }

        // If it is a zip file, then upload it as a package, else upload it as a yaml file
        if (originalFilename.endsWith(".zip")) {
            return uploadPackage(file, userId);
        } else if (originalFilename.endsWith(".yaml") || originalFilename.endsWith(".yml")) {
            return uploadYaml(file, userId);
        }

        throw new HolisticFaaSException("File type not supported: " + originalFilename);

    }

}
