package com.asi.hms.controller;

import com.asi.hms.model.api.APIWorkflow;
import com.asi.hms.model.api.APIWorkflowDeployment;
import com.asi.hms.service.WorkflowDeploymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workflow_deployment")
public class WorkflowDeploymentController {

    private final WorkflowDeploymentService workflowDeploymentService;

    public WorkflowDeploymentController(WorkflowDeploymentService workflowDeploymentService) {
        this.workflowDeploymentService = workflowDeploymentService;
    }


    @GetMapping(value = "/getAll")
    public ResponseEntity<List<APIWorkflowDeployment>> getAllWorkflows() {

        return ResponseEntity.ok(this.workflowDeploymentService.getAllWorkflowDeployments());

    }

    @GetMapping(value = "/get")
    public ResponseEntity<APIWorkflowDeployment> getWorkflow(@RequestParam UUID id) {

        return ResponseEntity.ok(this.workflowDeploymentService.getWorkflowDeployment(id));

    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> add(@RequestBody APIWorkflowDeployment apiWorkflowDeployment) {
        this.workflowDeploymentService.add(apiWorkflowDeployment);
        return ResponseEntity.ok("Workflow deployment added");
    }

}
