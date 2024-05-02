package com.asi.hms.controller;

import com.asi.hms.model.api.APIFunctionImplementation;
import com.asi.hms.model.api.APIFunctionType;
import com.asi.hms.model.api.APIWorkflow;
import com.asi.hms.model.api.APIWorkflowDeployment;
import com.asi.hms.service.WorkflowService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<APIWorkflow>> getAllWorkflows() {
        return ResponseEntity.ok(this.workflowService.getAllWorkflows());
    }

    @GetMapping(value = "/get")
    public ResponseEntity<APIWorkflow> getWorkflow(@RequestParam UUID id) {
        return ResponseEntity.ok(this.workflowService.getWorkflow(id));
    }

    @GetMapping(value = "/{workflowId}/getDeployments")
    public ResponseEntity<List<APIWorkflowDeployment>> getDeployments(@PathVariable UUID workflowId) {
        return ResponseEntity.ok(this.workflowService.getDeployments(workflowId));
    }

    @GetMapping(value = "/{workflowId}/implementations")
    public ResponseEntity<List<APIFunctionImplementation>> getWorkflowFunctionImplementations(@PathVariable UUID workflowId) {
        return ResponseEntity.ok(this.workflowService.getWorkflowFunctionImplementations(workflowId));
    }

    @GetMapping(value = "/{workflowId}/types")
    public ResponseEntity<List<APIFunctionType>> getWorkflowFunctionTypes(@PathVariable UUID workflowId) {
        return ResponseEntity.ok(this.workflowService.getWorkflowFunctionTypes(workflowId));
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIWorkflow> add(@RequestPart(value = "file", required = false) MultipartFile file,
                                                      @RequestPart(value = "workflow") APIWorkflow workflow) {

        return ResponseEntity.ok(this.workflowService.add(file, workflow));

    }

    @PostMapping(value = "{workflowId}/prepareWorkflowDeployment")
    public ResponseEntity<APIWorkflowDeployment> prepareWorkflowDeployment(@PathVariable UUID workflowId) {
        return ResponseEntity.ok(this.workflowService.prepareWorkflowDeployment(workflowId));
    }

    @PostMapping("{workflowId}/download")
    public ResponseEntity<byte[]> downloadAbstractWorkflow(@PathVariable UUID workflowId) {

        byte[] content = this.workflowService.downloadAbstractWorkflow(workflowId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=.yaml");

        return ResponseEntity.ok()
                .headers(headers)
                .body(content);

    }


}
