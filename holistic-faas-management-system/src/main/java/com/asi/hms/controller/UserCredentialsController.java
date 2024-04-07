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
    public ResponseEntity<String> add(@RequestPart("file") MultipartFile file,
                                         @RequestPart(value = "apiUser", required = false) APIUserCredentials apiUserCredentials){

        if(apiUserCredentials == null) {

            // TODO: remove this workaround

            // Workaround, because Swagger does only show the file input, but not the apiFunction input

            apiUserCredentials = new APIUserCredentials();

            String randomName = UUID.randomUUID().toString();

//            apiUserCredentials.setUsername(randomName);
            apiUserCredentials.setProvider("AWS"); // TODO


        }

        userCredentialsService.add(file, apiUserCredentials);

        return ResponseEntity.ok("Credentials added");

    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<APIUserCredentials>> getAllUser() {

        return ResponseEntity.ok(this.userCredentialsService.getAllUser());

    }

}
