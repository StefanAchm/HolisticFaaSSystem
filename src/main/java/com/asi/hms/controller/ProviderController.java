package com.asi.hms.controller;

import com.asi.hms.model.api.APIProviderOptions;
import com.asi.hms.service.ProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/provider")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping(value = "/getProviderOptions")
    public ResponseEntity<List<APIProviderOptions>> getRegions() {

        return ResponseEntity.ok(this.providerService.getProviderOptions());

    }

}
