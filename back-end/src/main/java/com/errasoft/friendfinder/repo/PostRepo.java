package com.errasoft.friendfinder.repo;

import com.errasoft.friendfinder.model.Post;
import com.errasoft.friendfinder.utils.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
