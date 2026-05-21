package kz.ablaysharimov.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovNotificationResponse;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovNotificationService;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Notifications", description = "Notification management endpoints")
public class AblaySharimovNotificationController {

    private final AblaySharimovNotificationService notificationService;
    private final AblaySharimovUserService userService;

    @GetMapping
    @Operation(summary = "Get my notifications", description = "Get paginated notifications for current user")
    public ResponseEntity<Page<AblaySharimovNotificationResponse>> getMyNotifications(Pageable pageable) {
        log.info("Getting notifications for current user");
        Long userId = userService.getCurrentUser().getId();
        Page<AblaySharimovNotificationResponse> notifications = notificationService.getMyNotifications(userId, pageable);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark as read", description = "Mark a notification as read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        log.info("Marking notification as read: {}", id);
        notificationService.markAsRead(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/read-all")
    @Operation(summary = "Mark all as read", description = "Mark all notifications as read for current user")
    public ResponseEntity<Void> markAllAsRead() {
        log.info("Marking all notifications as read");
        Long userId = userService.getCurrentUser().getId();
        notificationService.markAllAsRead(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Get unread count", description = "Get count of unread notifications for current user")
    public ResponseEntity<Long> getUnreadCount() {
        log.info("Getting unread notification count");
        Long userId = userService.getCurrentUser().getId();
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(count);
    }
}

