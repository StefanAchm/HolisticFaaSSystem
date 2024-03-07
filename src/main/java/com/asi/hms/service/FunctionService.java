package com.asi.hms.service;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.APIFunction;
import com.asi.hms.model.db.DBFunction;
import com.asi.hms.repository.FunctionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FunctionService {

    private static final Logger logger = LoggerFactory.getLogger(FunctionService.class);

    private final FunctionRepository functionRepository;

    public FunctionService(FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
    }

    public List<APIFunction> getAllFunction() {

        return this.functionRepository.findAll().stream().map(APIFunction::fromDBFunction).toList();

    }

    public void uploadFile(MultipartFile file, APIFunction apiFunction, Path rootLocation) {

        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            throw new HolisticFaaSException("File name is null");
        }

        // Create new folder
        Path destinationFolder = Paths.get(rootLocation.toString()).resolve(UUID.randomUUID().toString());

        try {

            Files.createDirectories(destinationFolder);

            logger.info("Upload directory created: {}", destinationFolder.toAbsolutePath());

        } catch (IOException e) {
            throw new HolisticFaaSException("Could not create directory " + destinationFolder);
        }

        Path destinationPath = Paths.get(destinationFolder.toString()).resolve(fileName);

        if (Files.exists(destinationPath)) {
            throw new HolisticFaaSException("File " + destinationPath + " already exists");
        }

        try {

            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            logger.info("File uploaded to: {}", destinationPath.toAbsolutePath());

        } catch (Exception e) {

            throw new HolisticFaaSException("Error uploading file, " + e.getMessage());

        }

        DBFunction dbFunction = new DBFunction();
        dbFunction.setFilePath(destinationPath.normalize().toString());
        dbFunction.setName(apiFunction.getName());

        functionRepository.save(dbFunction);

    }

}
