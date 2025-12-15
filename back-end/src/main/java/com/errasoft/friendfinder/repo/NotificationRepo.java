package com.errasoft.friendfinder.repo.security;

import com.errasoft.friendfinder.model.Notification;
import com.errasoft.friendfinder.model.security.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification,Long> {

    List<Notification> findByAccountOrderByCreatedDateDesc(Account account);

    List<Notification> findByAccountAndReadFalseOrderByCreatedDateDesc(Account account);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.account = :account AND n.read = false")
    void markAllAsReadByAccount(@Param("account") Account account);
}
