package kz.ablaysharimov.taskmanagementsystem.service.impl;

import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovNotificationResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovComment;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovNotification;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovNotificationType;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovTask;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovTaskStatus;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovUser;
import kz.ablaysharimov.taskmanagementsystem.exception.AblaySharimovResourceNotFoundException;
import kz.ablaysharimov.taskmanagementsystem.mapper.AblaySharimovNotificationMapper;
import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovNotificationRepository;
import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovTaskRepository;
import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovUserRepository;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AblaySharimovNotificationServiceImpl implements AblaySharimovNotificationService {

    private final AblaySharimovNotificationRepository notificationRepository;
    private final AblaySharimovTaskRepository taskRepository;
    private final AblaySharimovUserRepository userRepository;
    private final AblaySharimovNotificationMapper notificationMapper;

    @Override
    @Async
    public void sendTaskAssignmentNotification(AblaySharimovTask task, AblaySharimovUser assignee) {
        log.info("Sending task assignment notification for task: {} to user: {}", task.getId(), assignee.getId());

        AblaySharimovNotification notification = AblaySharimovNotification.builder()
                .message("You have been assigned to task: " + task.getTitle())
                .type(AblaySharimovNotificationType.TASK_ASSIGNED)
                .isRead(false)
                .recipient(assignee)
                .relatedTaskId(task.getId())
                .relatedProjectId(task.getProject().getId())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    @Async
    public void sendTaskUpdateNotification(AblaySharimovTask task) {
        log.info("Sending task update notification for task: {}", task.getId());

        if (task.getAssignee() != null) {
            AblaySharimovNotification notification = AblaySharimovNotification.builder()
                    .message("Task has been updated: " + task.getTitle())
                    .type(AblaySharimovNotificationType.TASK_UPDATED)
                    .isRead(false)
                    .recipient(task.getAssignee())
                    .relatedTaskId(task.getId())
                    .relatedProjectId(task.getProject().getId())
                    .build();

            notificationRepository.save(notification);
        }
    }

    @Override
    @Async
    public void sendCommentNotification(AblaySharimovComment comment) {
        log.info("Sending comment notification for comment: {}", comment.getId());

        AblaySharimovUser taskAssignee = comment.getTask().getAssignee();
        if (taskAssignee != null && !taskAssignee.getId().equals(comment.getAuthor().getId())) {
            AblaySharimovNotification notification = AblaySharimovNotification.builder()
                    .message("New comment on task: " + comment.getTask().getTitle())
                    .type(AblaySharimovNotificationType.COMMENT_ADDED)
                    .isRead(false)
                    .recipient(taskAssignee)
                    .relatedTaskId(comment.getTask().getId())
                    .relatedProjectId(comment.getTask().getProject().getId())
                    .build();

            notificationRepository.save(notification);
        }
    }

    @Override
    @Scheduled(cron = "0 0 9 * * ?")
    @Transactional
    public void checkAndSendDeadlineNotifications() {
        log.info("Checking for upcoming deadline tasks");

        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime endOfTomorrow = tomorrow.withHour(23).withMinute(59).withSecond(59);

        List<AblaySharimovTask> upcomingTasks = taskRepository.findByDueDateBefore(endOfTomorrow).stream()
                .filter(task -> task.getDueDate() != null &&
                               task.getDueDate().isAfter(LocalDateTime.now()) &&
                               !task.getStatus().equals(AblaySharimovTaskStatus.DONE) &&
                               !task.getStatus().equals(AblaySharimovTaskStatus.CANCELLED))
                .toList();

        for (AblaySharimovTask task : upcomingTasks) {
            if (task.getAssignee() != null) {
                AblaySharimovNotification notification = AblaySharimovNotification.builder()
                        .message("Task deadline approaching: " + task.getTitle())
                        .type(AblaySharimovNotificationType.DEADLINE_APPROACHING)
                        .isRead(false)
                        .recipient(task.getAssignee())
                        .relatedTaskId(task.getId())
                        .relatedProjectId(task.getProject().getId())
                        .build();

                notificationRepository.save(notification);
            }
        }

        log.info("Deadline notifications sent for {} tasks", upcomingTasks.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AblaySharimovNotificationResponse> getMyNotifications(Long userId, Pageable pageable) {
        log.debug("Getting notifications for user: {}", userId);
        return notificationRepository.findByRecipientId(userId, pageable)
                .map(notificationMapper::toResponse);
    }

    @Override
    public void markAsRead(Long notificationId) {
        log.info("Marking notification as read: {}", notificationId);
        AblaySharimovNotification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead(Long userId) {
        log.info("Marking all notifications as read for user: {}", userId);
        List<AblaySharimovNotification> unreadNotifications = notificationRepository.findByRecipientIdAndIsReadFalse(userId);
        for (AblaySharimovNotification notification : unreadNotifications) {
            notification.setIsRead(true);
        }
        notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        log.debug("Getting unread notification count for user: {}", userId);
        return notificationRepository.countByRecipientIdAndIsReadFalse(userId);
    }
}

