package com.asi.hms.controller;

import com.asi.hms.model.api.APIFunctionFlat;
import com.asi.hms.model.api.APIFunctionType;
import com.asi.hms.model.api.APIMigrationPreparation;
import com.asi.hms.model.api.APIMigration;
import com.asi.hms.service.FunctionFlatService;
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
@RequestMapping("/api/functionFlat")
public class FunctionFlatController {

    private final FunctionFlatService functionFlatService;

    public FunctionFlatController(FunctionFlatService functionFlatService) {
        this.functionFlatService = functionFlatService;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<APIFunctionFlat>> getAllFunctions() {
        return ResponseEntity.ok(this.functionFlatService.getAllFunctions());
    }

    @PostMapping(value = "/prepareMigration")
    public ResponseEntity<APIMigration> prepareMigration(@RequestBody APIMigrationPreparation apiMigrationPreparation) {
        return ResponseEntity.ok(this.functionFlatService.prepareMigration(apiMigrationPreparation));
    }

    @PostMapping(value = "/migrate")
    public ResponseEntity<APIMigration> migrate(@RequestBody APIMigration apiMigration) {
        return ResponseEntity.ok(this.functionFlatService.migrate(apiMigration));
    }


    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadYaml(@RequestBody List<APIFunctionFlat> functions) throws IOException {

        String yamlString = this.functionFlatService.getYaml(functions);

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
        return ResponseEntity.ok(this.functionFlatService.uploadPackage(file, userId));
    }

    @PostMapping("/uploadYaml")
    public ResponseEntity<List<APIFunctionType>> uploadYaml(@RequestPart("file") MultipartFile file, @RequestParam UUID userId) {
        return ResponseEntity.ok(this.functionFlatService.uploadYaml(file, userId));
    }

    @PostMapping("/upload")
    public ResponseEntity<List<APIFunctionType>> upload(@RequestPart("file") MultipartFile file, @RequestParam UUID userId) {
        return ResponseEntity.ok(this.functionFlatService.upload(file, userId));
    }

}
