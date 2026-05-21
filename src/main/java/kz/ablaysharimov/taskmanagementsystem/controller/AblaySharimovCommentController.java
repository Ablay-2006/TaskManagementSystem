package kz.ablaysharimov.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovCreateCommentRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovCommentResponse;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovCommentService;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Comments", description = "Comment management endpoints")
public class AblaySharimovCommentController {

    private final AblaySharimovCommentService commentService;
    private final AblaySharimovUserService userService;

    @PostMapping
    @Operation(summary = "Add comment", description = "Add a comment to a task")
    public ResponseEntity<AblaySharimovCommentResponse> addComment(@Valid @RequestBody AblaySharimovCreateCommentRequest request) {
        log.info("Adding comment to task: {}", request.getTaskId());
        Long userId = userService.getCurrentUser().getId();
        AblaySharimovCommentResponse comment = commentService.addComment(request, userId);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get task comments", description = "Get paginated comments for a task")
    public ResponseEntity<Page<AblaySharimovCommentResponse>> getCommentsByTask(
            @PathVariable Long taskId,
            Pageable pageable) {
        log.info("Getting comments for task: {}", taskId);
        Page<AblaySharimovCommentResponse> comments = commentService.getCommentsByTask(taskId, pageable);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update comment", description = "Update comment content")
    public ResponseEntity<AblaySharimovCommentResponse> updateComment(
            @PathVariable Long id,
            @RequestParam String content) {
        log.info("Updating comment: {}", id);
        Long userId = userService.getCurrentUser().getId();
        AblaySharimovCommentResponse comment = commentService.updateComment(id, content, userId);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete comment", description = "Delete a comment")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        log.info("Deleting comment: {}", id);
        Long userId = userService.getCurrentUser().getId();
        commentService.deleteComment(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

