package com.asi.hms.controller;

import com.asi.hms.model.api.APIWorkflowDeployment;
import com.asi.hms.model.api.APIWorkflowDeploymentMigration;
import com.asi.hms.model.api.APIWorkflowDeploymentMigrationResponse;
import com.asi.hms.service.WorkflowDeploymentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
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
    public ResponseEntity<List<APIWorkflowDeployment>> getAllWorkflowDeployments() {
        return ResponseEntity.ok(this.workflowDeploymentService.getAllWorkflowDeployments());
    }

    @GetMapping(value = "/get")
    public ResponseEntity<APIWorkflowDeployment> getWorkflowDeployment(@RequestParam UUID id) {
        return ResponseEntity.ok(this.workflowDeploymentService.getWorkflowDeployment(id));
    }

    @PostMapping(value = "/add")
    public ResponseEntity<APIWorkflowDeployment> add(@RequestBody APIWorkflowDeployment apiWorkflowDeployment) {
        return ResponseEntity.ok(this.workflowDeploymentService.add(apiWorkflowDeployment));
    }

    @PostMapping(value = "/migrate")
    public ResponseEntity<APIWorkflowDeploymentMigrationResponse> migrate(@RequestBody APIWorkflowDeploymentMigration apiWorkflowDeploymentMigration) {
        return ResponseEntity.ok(this.workflowDeploymentService.migrate(apiWorkflowDeploymentMigration));
    }

    @PostMapping("{workflowDeploymentId}/download")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable UUID workflowDeploymentId) {

        StreamingResponseBody stream = outputStream -> {
            try {
                this.workflowDeploymentService.download(workflowDeploymentId, outputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=.zip");

        return ResponseEntity.ok()
                .headers(headers)
                .body(stream);

    }

    @PostMapping("{workflowDeploymentId}/downloadYaml")
    public ResponseEntity<byte[]> downloadYaml(@PathVariable UUID workflowDeploymentId) {

        byte[] content;
        try {
            content = this.workflowDeploymentService.downloadYaml(workflowDeploymentId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=.yaml");

        return ResponseEntity.ok()
                .headers(headers)
                .body(content);

    }


}
