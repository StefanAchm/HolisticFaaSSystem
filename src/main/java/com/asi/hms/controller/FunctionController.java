package com.asi.hms.controller;

import com.asi.hms.components.FileStorageProperties;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.APIFunction;
import com.asi.hms.service.FunctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/function")
public class FunctionController {

    private static final Logger logger = LoggerFactory.getLogger(FunctionController.class);

    private final Path rootLocation;

    private final FunctionService functionService;

    public FunctionController(FunctionService functionService,
                              FileStorageProperties fileStorageProperties) {

        this.functionService = functionService;

        this.rootLocation = Paths.get(fileStorageProperties.getUploadDir());

        try {

            Path directories = Files.createDirectories(this.rootLocation);

            String absolutePath = directories.toAbsolutePath().toString();

            logger.info("Upload directory created: {}", absolutePath);

        } catch (Exception e) {

            throw new HolisticFaaSException("Could not create upload directory!");

        }

    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file,
                                             @RequestPart(value = "apiFunction", required = false) APIFunction apiFunction) {

        if(apiFunction == null) {

            // Workaround, because Swagger does only show the file input, but not the apiFunction input

            apiFunction = new APIFunction();

            // Random name
            String randomName = UUID.randomUUID().toString();
            apiFunction.setName(randomName);

        }

        this.functionService.uploadFile(file, apiFunction, this.rootLocation);

        return ResponseEntity.ok("File uploaded");

    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<APIFunction>> getAllFunction() {

        return ResponseEntity.ok(this.functionService.getAllFunction());

    }

}
