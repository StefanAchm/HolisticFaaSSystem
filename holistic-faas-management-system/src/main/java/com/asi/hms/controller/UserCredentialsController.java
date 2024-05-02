package com.asi.hms.controller;

import com.asi.hms.model.api.APIUserCredentials;
import com.asi.hms.service.UserCredentialsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user_credentials")
public class UserCredentialsController {

    private final UserCredentialsService userCredentialsService;

    public UserCredentialsController(UserCredentialsService userCredentialsService) {
        this.userCredentialsService = userCredentialsService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<APIUserCredentials> add(@RequestPart("file") MultipartFile file,
                                      @RequestPart(value = "apiUserCredentials") APIUserCredentials apiUserCredentials){

        return ResponseEntity.ok(this.userCredentialsService.addOrUpdate(file, apiUserCredentials, true));

    }

    @PostMapping(value = "/addOrUpdate")
    public ResponseEntity<APIUserCredentials> addOrUpdate(@RequestPart("file") MultipartFile file,
                                              @RequestPart(value = "apiUserCredentials") APIUserCredentials apiUserCredentials){
        return ResponseEntity.ok(this.userCredentialsService.addOrUpdate(file, apiUserCredentials, false));
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<APIUserCredentials>> getAllUser() {
        return ResponseEntity.ok(this.userCredentialsService.getAllUser());
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<APIUserCredentials>> get(@RequestParam UUID userId) {
        return ResponseEntity.ok(this.userCredentialsService.get(userId));
    }

}
