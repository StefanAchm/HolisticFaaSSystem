package com.asi.hms.controller;

import com.asi.hms.model.api.APIFunction;
import com.asi.hms.service.FunctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
