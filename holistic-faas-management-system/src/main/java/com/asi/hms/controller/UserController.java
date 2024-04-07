package com.asi.hms.controller;

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

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestParam String username) {
        this.userService.register(username);
        return ResponseEntity.ok("User created successfully");
    }

//    @GetMapping("/getAll")
//    public ResponseEntity<String> getAllUsers() {
//        return ResponseEntity.ok(this.userService.getAllUsers());
//    }

}
