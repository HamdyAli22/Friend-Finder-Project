package com.errasoft.friendfinder.repo;

import com.errasoft.friendfinder.model.Post;
import com.errasoft.friendfinder.utils.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<Post,Long> {
    Page<Post> findByOwnerIdInAndDeletedFalseOrderByCreatedDateDesc(List<Long> ownerIds, Pageable pageable);

    Optional<Post> findByIdAndDeletedFalse(Long id);
    // For delete check ownership quickly
    void deleteByIdAndOwnerId(Long postId, Long ownerId);

    List<Post> findByOwnerIdAndMediaTypeAndDeletedFalse(Long ownerId, MediaType mediaType);

    List<Post> findByOwnerIdAndDeletedFalse(Long ownerId);

    List<Post> findTop4ByOwnerIdAndDeletedFalseOrderByCreatedDateDesc(Long ownerId);

    @Query("""
        SELECT p FROM Post p
        WHERE p.deleted = false
          AND (
                LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR EXISTS (
                 SELECT 1 FROM Comment c
                 WHERE c.post.id = p.id
                   AND c.deleted = false
                   AND LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
             )
             OR LOWER(p.owner.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
        ORDER BY p.createdDate DESC
    """)
    Page<Post> searchPosts(String keyword, Pageable pageable);

}
