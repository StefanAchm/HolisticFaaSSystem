package com.asi.hms.controller;

import com.asi.hms.model.api.APIFunction;
import com.asi.hms.service.FunctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/function")
public class FunctionController {

    private final FunctionService functionService;

    public FunctionController(FunctionService functionService) {
        this.functionService = functionService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addFunction(APIFunction apiFunction) {

        this.functionService.addFunction(apiFunction);

        return ResponseEntity.ok("Function added");

    }


}
