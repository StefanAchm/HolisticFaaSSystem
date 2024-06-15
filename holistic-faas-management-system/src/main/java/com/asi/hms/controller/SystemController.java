package com.asi.hms.controller;

import com.asi.hms.model.api.APISystemInfo;
import com.asi.hms.service.SystemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    @RequestMapping("/info")
    public ResponseEntity<APISystemInfo> getSystemInfo() {
        return ResponseEntity.ok(this.systemService.getSystemInfo());
    }

}
