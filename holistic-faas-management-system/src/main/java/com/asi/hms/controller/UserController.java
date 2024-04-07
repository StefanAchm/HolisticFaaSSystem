package com.asi.hms.controller;

import com.asi.hms.model.api.APIAuthToken;
import com.asi.hms.model.api.APIUser;
import com.asi.hms.service.UserService;
import com.asi.hms.components.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public UserController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils,
                          UserService userService) {

        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;

    }

//    @PostMapping("/create")
//    public ResponseEntity<String> createUser(@RequestParam String username) {
//        this.userService.register(username);
//        return ResponseEntity.ok("User created successfully");
//    }

    @PostMapping("/register")
    public ResponseEntity<APIAuthToken> register(@RequestBody APIUser user) {

        this.userService.register(user);

        return login(user);

    }

    @PostMapping("/login")
    public ResponseEntity<APIAuthToken> login(@RequestBody APIUser user) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtils.generateToken(authentication.getName());

            return ResponseEntity.ok(new APIAuthToken(token));

        } catch (AuthenticationException e) {

            // Return the error message as a response

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new APIAuthToken(e.getMessage()));

//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .build();

        }

    }


//    @GetMapping("/getAll")
//    public ResponseEntity<String> getAllUsers() {
//        return ResponseEntity.ok(this.userService.getAllUsers());
//    }

}
