package com.asi.hms.controller;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.DeployFunction;
import com.asi.hms.model.Function;
import com.asi.hms.model.Role;
import com.asi.hms.service.DeployService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deploy")
public class DeployController {

    private final DeployService deployService;

    public DeployController(DeployService deployService) {
        this.deployService = deployService;
    }

    @PostMapping(value = "/function")
    public ResponseEntity<String> deployFunction(@RequestBody DeployFunction deployFunction) {

        try {

            this.deployService.deploy(deployFunction);

            return ResponseEntity.ok("Function deployed");


        } catch (HolisticFaaSException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
