package com.errasoft.friendfinder.repo;

import com.errasoft.friendfinder.model.Friendship;
import com.errasoft.friendfinder.model.security.Account;
import com.errasoft.friendfinder.utils.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepo extends JpaRepository<Friendship, Long> {

    List<Friendship> findByRequesterAndStatus(Account requester, FriendshipStatus status);
    List<Friendship> findByReceiverAndStatus(Account receiver, FriendshipStatus status);

    @Query("select f.receiver.id from Friendship f where f.requester.id = :accountId and f.status = 'ACCEPTED' " +
            "union " +
            "select f.requester.id from Friendship f where f.receiver.id = :accountId and f.status = 'ACCEPTED'")
    List<Long> findAcceptedFriendIds(Long accountId);

    Optional<Friendship> findByRequesterIdAndReceiverId(Long requesterId, Long receiverId);

    @Query("""
        SELECT COUNT(f) > 0 FROM Friendship f
        WHERE (
               (f.requester.id = :requesterId AND f.receiver.id = :receiverId)
               OR
               (f.requester.id = :receiverId AND f.receiver.id = :requesterId)
        )
        AND f.status IN (com.errasoft.friendfinder.utils.FriendshipStatus.PENDING,
                         com.errasoft.friendfinder.utils.FriendshipStatus.ACCEPTED)
        """)
    boolean existsActiveFriendship(Long requesterId, Long receiverId);

    @Query("""
    SELECT a FROM Account a
    WHERE a.enabled = true
      AND a.id <> :currentId
      AND NOT EXISTS (
         SELECT 1 FROM Friendship f
         WHERE (
             (f.receiver.id = :currentId AND f.requester.id = a.id AND f.status = 'PENDING')
             OR
             (f.status = 'ACCEPTED' AND (
                  (f.requester.id = :currentId AND f.receiver.id = a.id)
                  OR
                  (f.receiver.id = :currentId AND f.requester.id = a.id)
             ))
         )
      )
""")
    List<Account> findSuggestedUsers(Long currentId);


    @Query("""
    SELECT f FROM Friendship f
    WHERE 
       (
          (f.requester.id = :currentUserId AND f.receiver.id = :otherUserId)
          OR
          (f.requester.id = :otherUserId AND f.receiver.id = :currentUserId)
       )
       AND f.status IN ('PENDING', 'ACCEPTED')
""")
    Optional<Friendship> findActiveFriendship(
             Long currentUserId,
             Long otherUserId
    );

}
