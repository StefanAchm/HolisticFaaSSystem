package com.asi.hms.controller;

import com.asi.hms.model.api.APIUserCredentials;
import com.asi.hms.service.UserCredentialsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user_credentials")
public class UserCredentialsController {

    private final UserCredentialsService userCredentialsService;

    public UserCredentialsController(UserCredentialsService userCredentialsService) {
        this.userCredentialsService = userCredentialsService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> add(@RequestPart("file") MultipartFile file,
                                         @RequestPart(value = "apiUserCredentials", required = false) APIUserCredentials apiUserCredentials){

        this.userCredentialsService.add(file, apiUserCredentials);

        return ResponseEntity.ok("Credentials added");

    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<APIUserCredentials>> getAllUser() {

        return ResponseEntity.ok(this.userCredentialsService.getAllUser());

    }

}
