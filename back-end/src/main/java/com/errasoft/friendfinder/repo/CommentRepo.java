package com.errasoft.friendfinder.repo;

import com.errasoft.friendfinder.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostIdAndDeletedFalseOrderByCreatedDateDesc(Long id, Pageable pageable);

    Optional<Comment> findByIdAndDeletedFalse(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Comment c SET c.deleted = true WHERE c.post.id = :postId AND c.deleted = false")
    void softDeleteByPostId(Long postId);

    List<Comment> findTop4ByOwnerIdAndDeletedFalseOrderByCreatedDateDesc(Long ownerId);

}
