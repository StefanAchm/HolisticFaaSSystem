package com.asi.hms.controller;

import com.asi.hms.model.api.APIFunctionDeployment;
import com.asi.hms.service.DeployService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/deploy")
public class DeployController {

    private final DeployService deployService;

    public DeployController(DeployService deployService) {
        this.deployService = deployService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addFunctionDeployment(@RequestBody APIFunctionDeployment apiFunctionDeployment) {
        this.deployService.addFunctionDeployment(apiFunctionDeployment);
        return ResponseEntity.ok("Deploy Function added");
    }

    @PostMapping(value = "/deploy")
    public ResponseEntity<String> deploy(@RequestParam UUID functionId, @RequestParam boolean localOnly) {

        this.deployService.deploy(functionId, localOnly);

        return ResponseEntity.ok("Deploying function now");

    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<APIFunctionDeployment>> getAllFunctionDeployments() {

        return ResponseEntity.ok(this.deployService.getAllFunctionDeployments());

    }

}
