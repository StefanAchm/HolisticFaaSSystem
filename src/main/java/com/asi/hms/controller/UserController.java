package com.asi.hms.controller;

import com.asi.hms.model.api.APIUser;
import com.asi.hms.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUserAccount(@RequestBody APIUser APIUser) {

        return userService.registerUser(APIUser);

    }




}
