package com.asi.hms.controller;

import com.asi.hms.model.api.APIUser;
import com.asi.hms.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> create(@RequestPart("file") MultipartFile file,
                                         @RequestPart(value = "apiUser", required = false) APIUser apiUser ){

        if(apiUser == null) {

            // Workaround, because Swagger does only show the file input, but not the apiFunction input

            apiUser = new APIUser();

            String randomName = UUID.randomUUID().toString();

            apiUser.setUsername(randomName);
            apiUser.setProvider("AWS"); // TODO


        }

        userService.create(file, apiUser);

        return ResponseEntity.ok("Credentials added");

    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<APIUser>> getAllUser() {

        return ResponseEntity.ok(this.userService.getAllUser());

    }

}
