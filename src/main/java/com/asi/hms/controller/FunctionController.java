package com.asi.hms.controller;

import com.asi.hms.model.api.APIFunction;
import com.asi.hms.service.FunctionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/function")
public class FunctionController {

    private final FunctionService functionService;

    public FunctionController(FunctionService functionService) {

        this.functionService = functionService;

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

        this.functionService.uploadFile(file, apiFunction);

        return ResponseEntity.ok("File uploaded");

    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<APIFunction>> getAllFunction() {

        return ResponseEntity.ok(this.functionService.getAllFunction());

    }

}
