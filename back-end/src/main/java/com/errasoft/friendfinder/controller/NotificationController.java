package com.errasoft.friendfinder.controller;

import com.errasoft.friendfinder.dto.NotificationDto;
import com.errasoft.friendfinder.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/create")
    public ResponseEntity<NotificationDto> createNotification(@RequestBody NotificationDto notificationDto) {
        NotificationDto saved = notificationService.createNotification(notificationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/user")
    public ResponseEntity<List<NotificationDto>> getUserNotifications(
            @RequestParam String email) {
        List<NotificationDto> notifications = notificationService.getNotificationsByUser(email);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/user/unread")
    public ResponseEntity<List<NotificationDto>> getUnreadNotifications(
            @RequestParam String email) {
        List<NotificationDto> unread = notificationService.getUnreadNotifications(email);
        return ResponseEntity.ok(unread);
    }

    @PutMapping("/mark-read")
    public ResponseEntity<NotificationDto> markAsRead(
            @RequestParam Long id) {
        NotificationDto updated = notificationService.markAsRead(id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteNotificationById(@RequestParam Long id) {
        notificationService.deleteNotificationById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/mark-all-read")
    public ResponseEntity<Void> markAllAsRead(@RequestParam String email) {
        notificationService.markAllAsRead(email);
        return ResponseEntity.noContent().build();
    }

}
