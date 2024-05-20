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
    public ResponseEntity<APIFunctionDeployment> add(@RequestBody APIFunctionDeployment apiFunctionDeployment) {
        return ResponseEntity.ok(this.functionDeploymentService.add(apiFunctionDeployment));
    }

    @PostMapping(value = "/update")
    public ResponseEntity<APIFunctionDeployment> update(@RequestBody APIFunctionDeployment apiFunctionDeployment) {
        return ResponseEntity.ok(this.functionDeploymentService.update(apiFunctionDeployment));
    }

    @PostMapping(value = "/deploy")
    public ResponseEntity<String> deploy(@RequestParam UUID functionId,
                                         @RequestParam String awsSessionToken) {

        if(!this.functionDeploymentService.validateUser(functionId)) {
            return ResponseEntity.badRequest().body("Current user is not allowed to deploy this function!");
        }

        this.functionDeploymentService.deploy(functionId, awsSessionToken);
        return ResponseEntity.ok("Function deployment started");
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
