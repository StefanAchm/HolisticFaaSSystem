package com.asi.hms.controller;

import com.asi.hms.model.api.APIFunctionImplementation;
import com.asi.hms.service.FunctionImplementationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/function_implementation")
public class FunctionImplementationController {

    private final FunctionImplementationService functionImplementationService;

    public FunctionImplementationController(FunctionImplementationService functionImplementationService) {

        this.functionImplementationService = functionImplementationService;

    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> add(@RequestPart("file") MultipartFile file,
                                      @RequestPart(value = "apiFunction") APIFunctionImplementation apiFunctionImplementation) {

        this.functionImplementationService.add(file, apiFunctionImplementation);

        return ResponseEntity.ok("File uploaded");

    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<APIFunctionImplementation>> getAllFunctionImplementations() {

        return ResponseEntity.ok(this.functionImplementationService.getAllFunction());

    }

}
