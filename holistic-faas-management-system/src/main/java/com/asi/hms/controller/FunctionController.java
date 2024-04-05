package com.asi.hms.controller;

import com.asi.hms.model.api.APIFunction;
import com.asi.hms.model.api.APIMigration;
import com.asi.hms.model.api.APIPreparedMigration;
import com.asi.hms.service.FunctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value = "/prepareMigration")
    public ResponseEntity<APIPreparedMigration> prepareMigration(@RequestBody APIMigration apiMigration) {

        return ResponseEntity.ok(this.functionService.prepareMigration(apiMigration));

    }

}
