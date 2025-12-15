package com.errasoft.friendfinder.service;

import com.errasoft.friendfinder.dto.NotificationDto;
import com.errasoft.friendfinder.model.security.Account;

import java.util.List;


public interface NotificationService {
    NotificationDto createNotification(NotificationDto notificationDto);

    List<NotificationDto> getNotificationsByUser(String email);

    List<NotificationDto> getUnreadNotifications(String email);

    NotificationDto markAsRead(Long notificationId);

    void handleNotification(Account user, Account admin, String message,String type);

    void deleteNotificationById(Long id);

    void markAllAsRead(String email);

}
