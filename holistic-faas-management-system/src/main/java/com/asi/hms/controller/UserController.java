package com.asi.hms.controller;

import com.asi.hms.model.api.APILoginResponse;
import com.asi.hms.model.api.APIUser;
import com.asi.hms.service.UserService;
import com.asi.hms.config.security.JwtUtils;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody APIUser user) {

        if(this.userService.checkIfUserExists(user.getUsername())) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("User already exists");

        }

        return ResponseEntity.ok(this.userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<APILoginResponse> login(@RequestBody APIUser user) {

        try {

            Authentication authentication = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = this.jwtUtils.generateToken(authentication.getName());

            return ResponseEntity.ok(this.userService.login(user, token));

        } catch (AuthenticationException e) {

            // Return the error message as a response

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();

        }

    }


}
