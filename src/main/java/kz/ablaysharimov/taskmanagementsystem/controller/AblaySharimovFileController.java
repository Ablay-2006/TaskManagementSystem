package kz.ablaysharimov.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovAttachmentResponse;
import kz.ablaysharimov.taskmanagementsystem.service.impl.AblaySharimovFileStorageService;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Files", description = "File management endpoints")
public class AblaySharimovFileController {

    private final AblaySharimovFileStorageService fileStorageService;
    private final AblaySharimovUserService userService;

    @PostMapping("/upload/task/{taskId}")
    @Operation(summary = "Upload file", description = "Upload an attachment to a task")
    public ResponseEntity<AblaySharimovAttachmentResponse> uploadFile(
            @PathVariable Long taskId,
            @RequestParam MultipartFile file) {
        log.info("Uploading file for task: {}", taskId);
        Long userId = userService.getCurrentUser().getId();
        AblaySharimovAttachmentResponse response = fileStorageService.storeFile(file, taskId, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/download/{attachmentId}")
    @Operation(summary = "Download file", description = "Download an attachment file")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long attachmentId) {
        log.info("Downloading file: {}", attachmentId);
        Resource resource = fileStorageService.loadFileAsResource(attachmentId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{attachmentId}")
    @Operation(summary = "Delete file", description = "Delete an attachment")
    public ResponseEntity<Void> deleteFile(@PathVariable Long attachmentId) {
        log.info("Deleting file: {}", attachmentId);
        fileStorageService.deleteFile(attachmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get task attachments", description = "Get all attachments for a task")
    public ResponseEntity<List<AblaySharimovAttachmentResponse>> getTaskAttachments(@PathVariable Long taskId) {
        log.info("Getting attachments for task: {}", taskId);
        List<AblaySharimovAttachmentResponse> attachments = fileStorageService.getAttachmentsByTask(taskId);
        return ResponseEntity.ok(attachments);
    }
}

