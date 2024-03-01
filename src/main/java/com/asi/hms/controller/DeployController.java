package com.asi.hms.controller;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.DeployFunction;
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
    public ResponseEntity<String> deployFunction(@RequestBody DeployFunction deployFunction) {

        this.deployService.deploy(deployFunction);

        return ResponseEntity.ok("Function deployed");

    }

}
