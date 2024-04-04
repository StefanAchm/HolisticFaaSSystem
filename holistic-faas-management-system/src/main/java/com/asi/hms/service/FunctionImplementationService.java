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

        return this.functionImplementationRepository.findAll().stream().map(APIFunctionImplementation::fromDBFunctionImplementation).toList();

    }

    public void add(MultipartFile file, APIFunctionImplementation apiFunctionImplementation) {

        String path = this.uploadFileService.uploadFileAndNormalize(file, UploadFileService.FUNCTIONS_DIR);

        DBFunctionImplementation dbFunctionImplementation = new DBFunctionImplementation();
        dbFunctionImplementation.setFilePath(path);

        DBFunctionType dbFunctionType = this.functionTypeRepository.findById(apiFunctionImplementation.getFunctionTypeId())
                .orElseThrow(() -> new HolisticFaaSException("Functiontype not found"));

        dbFunctionImplementation.setFunctionType(dbFunctionType);

        this.functionImplementationRepository.save(dbFunctionImplementation);

    }



}
