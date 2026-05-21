package kz.ablaysharimov.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovUserResponse;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Users", description = "User management endpoints")
public class AblaySharimovUserController {

    private final AblaySharimovUserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Get paginated list of active users")
    public ResponseEntity<Page<AblaySharimovUserResponse>> getAllUsers(Pageable pageable) {
        log.info("Getting all users");
        Page<AblaySharimovUserResponse> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve user details by ID")
    public ResponseEntity<AblaySharimovUserResponse> getUserById(@PathVariable Long id) {
        log.info("Getting user by id: {}", id);
        AblaySharimovUserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get authenticated user details")
    public ResponseEntity<AblaySharimovUserResponse> getCurrentUser() {
        log.info("Getting current user");
        AblaySharimovUserResponse user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityChecker.isCurrentUser(#id)")
    @Operation(summary = "Update user", description = "Update user email")
    public ResponseEntity<AblaySharimovUserResponse> updateUser(@PathVariable Long id, @RequestParam String email) {
        log.info("Updating user: {}", id);
        AblaySharimovUserResponse user = userService.updateUser(id, email);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate user", description = "Deactivate user account")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        log.info("Deactivating user: {}", id);
        userService.deactivateUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/me/profile-picture")
    @Operation(summary = "Upload profile picture", description = "Upload user profile picture")
    public ResponseEntity<AblaySharimovUserResponse> uploadProfilePicture(@RequestParam MultipartFile file) {
        log.info("Uploading profile picture");
        AblaySharimovUserResponse user = userService.getCurrentUser();
        AblaySharimovUserResponse updated = userService.uploadProfilePicture(user.getId(), file);
        return ResponseEntity.ok(updated);
    }
}

