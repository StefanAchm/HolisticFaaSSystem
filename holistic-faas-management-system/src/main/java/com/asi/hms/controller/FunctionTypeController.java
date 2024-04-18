package com.asi.hms.controller;

import com.asi.hms.model.api.APIFunctionType;
import com.asi.hms.service.FunctionTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/function_type")
public class FunctionTypeController {

    private final FunctionTypeService functionTypeService;

    public FunctionTypeController(FunctionTypeService functionTypeService) {
        this.functionTypeService = functionTypeService;
    }

    @PostMapping(value = "/add")
    public void addFunctionType(@RequestBody APIFunctionType apiFunctionType) {
        this.functionTypeService.add(apiFunctionType);
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<APIFunctionType>> getAllFunctionTypes() {
        return ResponseEntity.ok(this.functionTypeService.getAllFunctionTypes());
    }



}
