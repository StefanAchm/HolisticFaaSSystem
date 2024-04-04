package com.asi.hms.controller;

import com.asi.hms.model.api.APIFunctionDeployment;
import com.asi.hms.service.FunctionDeploymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/function_deployment")
public class FunctionDeploymentController {

    private final FunctionDeploymentService functionDeploymentService;

    public FunctionDeploymentController(FunctionDeploymentService functionDeploymentService) {
        this.functionDeploymentService = functionDeploymentService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addFunctionDeployment(@RequestBody APIFunctionDeployment apiFunctionDeployment) {
        this.functionDeploymentService.addFunctionDeployment(apiFunctionDeployment);
        return ResponseEntity.ok("Deploy Function added");
    }

    @PostMapping(value = "/deploy")
    public ResponseEntity<String> deploy(@RequestParam UUID functionId, @RequestParam boolean localOnly) {

        this.functionDeploymentService.deploy(functionId, localOnly);

        return ResponseEntity.ok("Deploying function now");

    }

    @GetMapping(value = "/get")
    public ResponseEntity<APIFunctionDeployment> getFunctionDeployment(@RequestParam UUID functionId) {

        return ResponseEntity.ok(this.functionDeploymentService.getFunctionDeployment(functionId));

    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<APIFunctionDeployment>> getAllFunctionDeployments() {

        return ResponseEntity.ok(this.functionDeploymentService.getAllFunctionDeployments());

    }

}
