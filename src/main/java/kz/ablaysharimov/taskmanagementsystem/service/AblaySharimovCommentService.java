package kz.ablaysharimov.taskmanagementsystem.service;

import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovCreateCommentRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AblaySharimovCommentService {
    AblaySharimovCommentResponse addComment(AblaySharimovCreateCommentRequest request, Long authorId);
    Page<AblaySharimovCommentResponse> getCommentsByTask(Long taskId, Pageable pageable);
    AblaySharimovCommentResponse updateComment(Long id, String content, Long requesterId);
    void deleteComment(Long id, Long requesterId);
}

