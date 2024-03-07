package com.asi.hms.service;

import com.asi.hms.model.api.APIFunction;
import com.asi.hms.model.db.DBFunction;
import com.asi.hms.repository.FunctionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@Service
public class FunctionService {

    private final FunctionRepository functionRepository;

    private final UploadFileService uploadFileService;

    public FunctionService(FunctionRepository functionRepository, UploadFileService uploadFileService) {
        this.functionRepository = functionRepository;
        this.uploadFileService = uploadFileService;
    }

    public List<APIFunction> getAllFunction() {

        return this.functionRepository.findAll().stream().map(APIFunction::fromDBFunction).toList();

    }

    public void uploadFile(MultipartFile file, APIFunction apiFunction) {

        String path = this.uploadFileService.uploadFileAndNormalize(file, UploadFileService.FUNCTIONS_DIR);

        DBFunction dbFunction = new DBFunction();
        dbFunction.setFilePath(path);
        dbFunction.setName(apiFunction.getName());

        this.functionRepository.save(dbFunction);

    }



}
