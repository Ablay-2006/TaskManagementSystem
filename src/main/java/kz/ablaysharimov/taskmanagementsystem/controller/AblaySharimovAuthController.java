package kz.ablaysharimov.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovLoginRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovRegisterRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovAuthResponse;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "User authentication endpoints")
public class AblaySharimovAuthController {

    private final AblaySharimovUserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account")
    public ResponseEntity<AblaySharimovAuthResponse> register(@Valid @RequestBody AblaySharimovRegisterRequest request) {
        log.info("Register request for username: {}", request.getUsername());
        AblaySharimovAuthResponse response = userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and get JWT token")
    public ResponseEntity<AblaySharimovAuthResponse> login(@Valid @RequestBody AblaySharimovLoginRequest request) {
        log.info("Login request for username: {}", request.getUsername());
        AblaySharimovAuthResponse response = userService.loginUser(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

