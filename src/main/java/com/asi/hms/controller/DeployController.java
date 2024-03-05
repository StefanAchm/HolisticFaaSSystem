package com.asi.hms.controller;

import com.asi.hms.model.api.APIFunction;
import com.asi.hms.service.DeployService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deploy")
public class DeployController {

    private final DeployService deployService;

    public DeployController(DeployService deployService) {
        this.deployService = deployService;
    }

    @PostMapping(value = "/function")
    public ResponseEntity<String> deployFunction(@RequestBody APIFunction apiFunction) {

        this.deployService.deploy(apiFunction);

        return ResponseEntity.ok("Function deployed");

    }

}
