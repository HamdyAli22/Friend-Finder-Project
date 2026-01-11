package com.errasoft.friendfinder.service.impl;

import com.errasoft.friendfinder.dto.NotificationDto;
import com.errasoft.friendfinder.dto.security.AccountDto;
import com.errasoft.friendfinder.mapper.security.AccountMapper;
import com.errasoft.friendfinder.mapper.NotificationMapper;
import com.errasoft.friendfinder.model.Notification;
import com.errasoft.friendfinder.model.security.Account;
import com.errasoft.friendfinder.repo.security.AccountRepo;
import com.errasoft.friendfinder.repo.NotificationRepo;
import com.errasoft.friendfinder.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepo notificationRepo;
    private AccountRepo accountRepo;
    private NotificationMapper notificationMapper;
    private AccountMapper accountMapper;

    @Autowired
    public NotificationServiceImpl(NotificationRepo notificationRepo, AccountRepo accountRepo, NotificationMapper notificationMapper, AccountMapper accountMapper) {
        this.notificationRepo = notificationRepo;
        this.accountRepo = accountRepo;
        this.notificationMapper = notificationMapper;
        this.accountMapper = accountMapper;
    }

    @Override
    public NotificationDto createNotification(NotificationDto notificationDto) {

        if(Objects.nonNull(notificationDto.getId())){
            throw new RuntimeException("id.must_be.null");
        }

        Notification notification = notificationMapper.toNotification(notificationDto);

        if(notificationDto.getAccount() == null){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            AccountDto accountDto = (AccountDto) auth.getPrincipal();
            Account account = accountMapper.toAccount(accountDto);
            notification.setAccount(account);
        }else{
            Account account = accountMapper.toAccount(notificationDto.getAccount());
            notification.setAccount(account);
        }
        notification.setCreatedDate(LocalDateTime.now());
        Notification saved = notificationRepo.save(notification);
        return notificationMapper.toNotificationDto(saved);
    }

    @Override
    public List<NotificationDto> getNotificationsByUser(String email) {
        Account account = accountRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("account.username.notExists"));
        List<Notification> notifications = notificationRepo.findByAccountAndDeletedFalseOrderByCreatedDateDesc(account);
        if(notifications.isEmpty()){
            throw new RuntimeException("notifications.not.found");
        }
        return notificationMapper.toNotificationDtoList(notifications);
    }

    @Override
    public List<NotificationDto> getUnreadNotifications(String email) {
        Account account = accountRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("account.username.notExists"));
        List<Notification> notifications = notificationRepo.findByAccountAndReadFalseAndDeletedFalseOrderByCreatedDateDesc(account);
        if(notifications.isEmpty()){
            throw new RuntimeException("notifications.not.found");
        }
        return notificationMapper.toNotificationDtoList(notifications);
    }

    @Override
    public NotificationDto markAsRead(Long notificationId) {

        Notification notification = notificationRepo.findByIdAndDeletedFalse(notificationId)
                .orElseThrow(() -> new RuntimeException("notification.not.found"));
        notification.setRead(true);
        notification.setUpdatedDate(LocalDateTime.now());
        Notification updated = notificationRepo.save(notification);
        return notificationMapper.toNotificationDto(updated);
    }

    @Override
    public void handleNotification(Account requester, Account receiver, String message,String type,Long postId) {

        switch (type){
            case "NEW_REQUEST":
                saveNotification(receiver,
                        "User " + requester.getUsername() + " sent you friend request",
                         type,null);
                break;

            case "ACCEPT_REQUEST":
                saveNotification(requester,
                        "User " + receiver.getUsername() + " accepted your friend request",
                        type,null);
                break;

            case "REACT":
                saveNotification(receiver,
                        "User " + requester.getUsername() + " reacted to your post",
                        type,postId);
                break;

            case "COMMENT":
                saveNotification(receiver,
                        "User " + requester.getUsername() + " commented on your post",
                        type,postId);
                break;

            default:
                throw new IllegalArgumentException("Unknown notification type: " + type);
        }
    }

    private void saveNotification(Account account, String message, String type, Long postId) {
        Notification notification = new Notification();
        notification.setType(type);
        notification.setAccount(account);
        notification.setMessage(message);
        notification.setPostId(postId);
        notification.setRead(false);
        notification.setDeleted(false);
        notification.setCreatedDate(LocalDateTime.now());
        notificationRepo.save(notification);
    }

    public void saveNotifications(List<Account> accounts, String message, String type) {
        if (accounts == null || accounts.isEmpty()) return;

        List<Notification> notifications = accounts.stream()
                .map(acc -> {
                    Notification notif = new Notification();
                    notif.setType(type);
                    notif.setAccount(acc);
                    notif.setMessage(message);
                    notif.setRead(false);
                    return notif;
                })
                .toList();

        notificationRepo.saveAll(notifications);
    }

    @Override
    public void deleteNotificationById(Long id) {

        Notification notification = notificationRepo.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("notification.not.found"));

        notification.setDeleted(true);

        notification.setUpdatedDate(LocalDateTime.now());

        notificationRepo.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(String email) {
        Account account = accountRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("account.username.notExists"));

        notificationRepo.markAllAsReadByAccount(account);
    }
}
