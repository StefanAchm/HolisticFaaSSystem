package com.asi.hms.controller;

import com.asi.hms.model.api.APIFunction;
import com.asi.hms.model.api.APIFunctionType;
import com.asi.hms.model.api.APIMigrationPreparation;
import com.asi.hms.model.api.APIMigration;
import com.asi.hms.service.FunctionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/function")
public class FunctionController {

    private final FunctionService functionService;

    public FunctionController(FunctionService functionService) {
        this.functionService = functionService;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<APIFunction>> getAllFunctions() {

        return ResponseEntity.ok(this.functionService.getAllFunctions());

    }

    @PostMapping(value = "/prepareMigration")
    public ResponseEntity<APIMigration> prepareMigration(@RequestBody APIMigrationPreparation apiMigrationPreparation) {

        return ResponseEntity.ok(this.functionService.prepareMigration(apiMigrationPreparation));

    }

    @PostMapping(value = "/migrate")
    public ResponseEntity<APIMigration> migrate(@RequestBody APIMigration apiMigration) {

        return ResponseEntity.ok(this.functionService.migrate(apiMigration));

    }


    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadYaml(@RequestBody List<APIFunction> functions) throws IOException {

        String yamlString = this.functionService.getYaml(functions);

        // TODO: Can this also be sent as blob?

        Path tempFile = Files.createTempFile("functions", ".yaml");

        Files.write(tempFile, yamlString.getBytes());

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=functions.yaml");

        byte[] fileContent = Files.readAllBytes(tempFile);

        Files.delete(tempFile);

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }

    @PostMapping("/uploadPackage")
    public ResponseEntity<List<APIFunctionType>> uploadPackage(@RequestParam("file") MultipartFile file, @RequestParam UUID userId) {

        List<APIFunctionType> functions = this.functionService.uploadPackage(file, userId);

        return ResponseEntity.ok(functions);

    }

    @PostMapping("/uploadYaml")
    public ResponseEntity<List<APIFunctionType>> uploadYaml(@RequestPart("file") MultipartFile file, @RequestParam UUID userId) {

        List<APIFunctionType> functions = this.functionService.uploadYaml(file, userId);

        return ResponseEntity.ok(functions);

    }

    @PostMapping("/upload")
    public ResponseEntity<List<APIFunctionType>> upload(@RequestPart("file") MultipartFile file, @RequestParam UUID userId) {

        List<APIFunctionType> functions = this.functionService.upload(file, userId);

        return ResponseEntity.ok(functions);

    }

}
