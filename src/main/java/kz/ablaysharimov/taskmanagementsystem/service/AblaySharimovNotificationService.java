package kz.ablaysharimov.taskmanagementsystem.service;

import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovNotificationResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovComment;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovTask;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AblaySharimovNotificationService {
    void sendTaskAssignmentNotification(AblaySharimovTask task, AblaySharimovUser assignee);
    void sendTaskUpdateNotification(AblaySharimovTask task);
    void sendCommentNotification(AblaySharimovComment comment);
    void checkAndSendDeadlineNotifications();
    Page<AblaySharimovNotificationResponse> getMyNotifications(Long userId, Pageable pageable);
    void markAsRead(Long notificationId);
    void markAllAsRead(Long userId);
    long getUnreadCount(Long userId);
}

