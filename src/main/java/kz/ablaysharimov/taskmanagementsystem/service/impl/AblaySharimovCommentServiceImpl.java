package kz.ablaysharimov.taskmanagementsystem.service.impl;

import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovCreateCommentRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovCommentResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovComment;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovTask;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovUser;
import kz.ablaysharimov.taskmanagementsystem.exception.AblaySharimovForbiddenException;
import kz.ablaysharimov.taskmanagementsystem.exception.AblaySharimovResourceNotFoundException;
import kz.ablaysharimov.taskmanagementsystem.mapper.AblaySharimovCommentMapper;
import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovCommentRepository;
import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovTaskRepository;
import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovUserRepository;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovCommentService;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AblaySharimovCommentServiceImpl implements AblaySharimovCommentService {

    private final AblaySharimovCommentRepository commentRepository;
    private final AblaySharimovTaskRepository taskRepository;
    private final AblaySharimovUserRepository userRepository;
    private final AblaySharimovCommentMapper commentMapper;
    private final AblaySharimovNotificationService notificationService;

    @Override
    public AblaySharimovCommentResponse addComment(AblaySharimovCreateCommentRequest request, Long authorId) {
        log.info("Adding comment to task: {}", request.getTaskId());

        AblaySharimovTask task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Task not found"));

        AblaySharimovUser author = userRepository.findById(authorId)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("User not found"));

        AblaySharimovComment comment = AblaySharimovComment.builder()
                .content(request.getContent())
                .task(task)
                .author(author)
                .build();

        AblaySharimovComment savedComment = commentRepository.save(comment);
        log.info("Comment added with id: {}", savedComment.getId());

        notificationService.sendCommentNotification(savedComment);
        return commentMapper.toResponse(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AblaySharimovCommentResponse> getCommentsByTask(Long taskId, Pageable pageable) {
        log.debug("Getting comments for task: {}", taskId);
        return commentRepository.findByTaskIdOrderByCreatedAtDesc(taskId, pageable)
                .map(commentMapper::toResponse);
    }

    @Override
    public AblaySharimovCommentResponse updateComment(Long id, String content, Long requesterId) {
        log.info("Updating comment: {}", id);
        AblaySharimovComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Comment not found"));

        if (!comment.getAuthor().getId().equals(requesterId)) {
            throw new AblaySharimovForbiddenException("You can only edit your own comments");
        }

        comment.setContent(content);
        AblaySharimovComment updated = commentRepository.save(comment);
        return commentMapper.toResponse(updated);
    }

    @Override
    public void deleteComment(Long id, Long requesterId) {
        log.info("Deleting comment: {}", id);
        AblaySharimovComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Comment not found"));

        if (!comment.getAuthor().getId().equals(requesterId)) {
            throw new AblaySharimovForbiddenException("You can only delete your own comments");
        }

        commentRepository.deleteById(id);
    }
}

