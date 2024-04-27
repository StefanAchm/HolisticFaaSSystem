package com.asi.hms.controller;

import com.asi.hms.model.api.APIFunctionImplementation;
import com.asi.hms.model.api.APIFunctionType;
import com.asi.hms.model.api.APIWorkflow;
import com.asi.hms.model.api.APIWorkflowDeployment;
import com.asi.hms.service.WorkflowService;
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
        List<APIFunctionImplementation> implementations = this.workflowService.getWorkflowFunctionImplementations(workflowId);
        return ResponseEntity.ok(implementations);
    }

    @GetMapping(value = "/{workflowId}/types")
    public ResponseEntity<List<APIFunctionType>> getWorkflowFunctionTypes(@PathVariable UUID workflowId) {
        List<APIFunctionType> types = this.workflowService.getWorkflowFunctionTypes(workflowId);
        return ResponseEntity.ok(types);
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIWorkflow> add(@RequestPart("file") MultipartFile file) {

        APIWorkflow workflow = this.workflowService.add(file);

        return ResponseEntity.ok(workflow);

    }

    @PostMapping(value = "{workflowId}/prepareWorkflowDeployment")
    public ResponseEntity<APIWorkflowDeployment> prepareWorkflowDeployment(@PathVariable UUID workflowId) {

        APIWorkflowDeployment deployment = this.workflowService.prepareWorkflowDeployment(workflowId);

        return ResponseEntity.ok(deployment);

    }
}
