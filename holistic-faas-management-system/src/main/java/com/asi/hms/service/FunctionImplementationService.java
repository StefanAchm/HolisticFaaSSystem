package com.asi.hms.service;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.APIFunctionImplementation;
import com.asi.hms.model.db.DBFunctionImplementation;
import com.asi.hms.model.db.DBFunctionType;
import com.asi.hms.repository.FunctionImplementationRepository;
import com.asi.hms.repository.FunctionTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FunctionImplementationService {

    private final FunctionImplementationRepository functionImplementationRepository;
    private final FunctionTypeRepository functionTypeRepository;
    private final UploadFileService uploadFileService;

    public FunctionImplementationService(FunctionImplementationRepository functionImplementationRepository,
                                         FunctionTypeRepository functionTypeRepository,
                                         UploadFileService uploadFileService) {

        this.functionImplementationRepository = functionImplementationRepository;
        this.functionTypeRepository = functionTypeRepository;
        this.uploadFileService = uploadFileService;

    }

    public List<APIFunctionImplementation> getAllFunction() {

        return this.functionImplementationRepository
                .findAll()
                .stream()
                .map(APIFunctionImplementation::fromDBFunctionImplementation).toList();

    }

    public APIFunctionImplementation add(MultipartFile file, APIFunctionImplementation apiFunctionImplementation) {

        String path = null;
        if(file != null) {
            path = this.uploadFileService.uploadFileAndNormalize(file, UploadFileService.FUNCTIONS_DIR);
        }

        DBFunctionImplementation dbFunctionImplementation = new DBFunctionImplementation();
        dbFunctionImplementation.setFilePath(path);

        DBFunctionType dbFunctionType = this.functionTypeRepository
                .findById(apiFunctionImplementation.getFunctionTypeId())
                .orElseThrow(() -> new HolisticFaaSException("Functiontype not found"));

        dbFunctionImplementation.setFunctionType(dbFunctionType);

        this.functionImplementationRepository.save(dbFunctionImplementation);

        return APIFunctionImplementation.fromDBFunctionImplementation(dbFunctionImplementation);

    }

    public void update(MultipartFile file, APIFunctionImplementation apiFunctionImplementation) {

        DBFunctionImplementation dbFunctionImplementation = this.functionImplementationRepository
                .findById(apiFunctionImplementation.getId())
                .orElseThrow(() -> new HolisticFaaSException("Function not found"));

        // TODO: delete old file
        String path = this.uploadFileService.uploadFileAndNormalize(file, UploadFileService.FUNCTIONS_DIR);

        dbFunctionImplementation.setFilePath(path);
        this.functionImplementationRepository.save(dbFunctionImplementation);

    }



}
