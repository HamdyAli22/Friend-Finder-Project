package com.errasoft.friendfinder.repo;

import com.errasoft.friendfinder.model.Reaction;
import com.errasoft.friendfinder.utils.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepo extends JpaRepository<Reaction, Long> {
    Reaction findByPostIdAndOwnerIdAndDeletedFalse(Long postId, Long ownerId);

    Long countByPostIdAndTypeAndDeletedFalse(Long postId, ReactionType type);

    List<Reaction> findTop4ByOwnerIdAndDeletedFalseOrderByCreatedDateDesc(Long ownerId);

    List<Reaction> findByPostIdAndTypeAndDeletedFalseOrderByCreatedDateDesc(Long postId, ReactionType type);

}
