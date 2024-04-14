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
    public ResponseEntity<String> add(@RequestBody APIFunctionDeployment apiFunctionDeployment) {
        this.functionDeploymentService.add(apiFunctionDeployment);
        return ResponseEntity.ok("Deploy Function added");
    }

    @PostMapping(value = "/update")
    public ResponseEntity<String> update(@RequestBody APIFunctionDeployment apiFunctionDeployment) {
        this.functionDeploymentService.update(apiFunctionDeployment);
        return ResponseEntity.ok("Deploy Function edited");
    }

    @PostMapping(value = "/deploy")
    public ResponseEntity<String> deploy(@RequestParam UUID functionId) {

        this.functionDeploymentService.deploy(functionId);

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
