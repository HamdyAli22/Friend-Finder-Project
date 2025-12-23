package com.errasoft.friendfinder.service.impl;

import com.errasoft.friendfinder.controller.vm.PostResponseVm;
import com.errasoft.friendfinder.dto.PostDto;
import com.errasoft.friendfinder.dto.security.AccountDto;
import com.errasoft.friendfinder.mapper.PostMapper;
import com.errasoft.friendfinder.model.Post;
import com.errasoft.friendfinder.repo.CommentRepo;
import com.errasoft.friendfinder.repo.FriendshipRepo;
import com.errasoft.friendfinder.repo.PostRepo;
import com.errasoft.friendfinder.service.FileUploadService;
import com.errasoft.friendfinder.service.PostService;
import com.errasoft.friendfinder.service.security.AccountService;
import com.errasoft.friendfinder.service.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class PostServiceImpl implements PostService {

    private  PostRepo postRepo;
    private  PostMapper postMapper;
    private  FileUploadService fileStorageService;
    private  FriendshipRepo friendshipRepo;
    private  AccountService accountService;
    private AuthService authService;
    private CommentRepo commentRepo;

    @Autowired
    public PostServiceImpl(PostRepo postRepo,
                           PostMapper postMapper,
                           FileUploadService fileStorageService,
                           FriendshipRepo friendshipRepo,
                           AccountService accountService,
                           AuthService authService,
                           CommentRepo commentRepo) {
        this.postRepo = postRepo;
        this.postMapper = postMapper;
        this.fileStorageService = fileStorageService;
        this.friendshipRepo = friendshipRepo;
        this.accountService = accountService;
        this.authService = authService;
        this.commentRepo = commentRepo;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        if (Objects.nonNull(postDto.getId())) {
            throw new RuntimeException("id.must.be.null");
        }

        AccountDto owner = authService.getCurrentUser();
        postDto.setOwner(owner);

        postDto.setCreatedDate(LocalDateTime.now());
        postDto.setLikesCount(0L);
        postDto.setCommentsCount(0L);

        Post post = postMapper.toPost(postDto);
        Post savedPost = postRepo.save(post);

        return postMapper.toPostDto(savedPost);

    }

    @Override
    public PostDto updatePost(PostDto postDto) {

        if (postDto.getId() == null) {
            throw new RuntimeException("id.must_be.not_null");
        }

        Post post = postRepo.findByIdAndDeletedFalse(postDto.getId())
                .orElseThrow(() -> new RuntimeException("post.not.found"));

        Long userId = authService.getCurrentUserId();

        if (!post.getOwner().getId().equals(userId)) {
            throw new RuntimeException("unauthorized.action");
        }

        if (postDto.getContent() != null) {
            post.setContent(postDto.getContent());
        }

        if (postDto.getMediaUrl() != null) {
            post.setMediaUrl(postDto.getMediaUrl());
            post.setMediaType(postDto.getMediaType());
        }

        Post updatedPost = postRepo.save(post);

        return postMapper.toPostDto(updatedPost);

    }

    @Override
    public PostResponseVm getFeed(int page, int size) {
        Long userId = authService.getCurrentUserId();

        List<Long> friendIds = friendshipRepo.findAcceptedFriendIds(userId);
        friendIds.add(userId);


        Pageable pageable = getPageable(page, size);

        Page<Post> posts = postRepo.findByOwnerIdInAndDeletedFalseOrderByCreatedDateDesc(friendIds, pageable);

        List<PostDto> postDtos = postMapper.toPostDtoList(posts.getContent());

        return new PostResponseVm(postDtos, posts.getTotalElements());

    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepo.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("post.not.found"));
        return postMapper.toPostDto(post);
    }

    @Override
    public PostResponseVm getMyPosts(int page, int size) {

        Long userId = authService.getCurrentUserId();

        Pageable pageable = getPageable(page, size);

        Page<Post> posts = postRepo.findByOwnerIdInAndDeletedFalseOrderByCreatedDateDesc(List.of(userId), pageable);

        List<PostDto> postDtos = postMapper.toPostDtoList(posts.getContent());

        return new PostResponseVm(postDtos, posts.getTotalElements());
    }

    @Override
    public PostResponseVm getUserPosts(int page, int size, int userId) {

        Pageable pageable = getPageable(page, size);

        Page<Post> posts = postRepo
                .findByOwnerIdInAndDeletedFalseOrderByCreatedDateDesc(
                        List.of((long) userId),
                        pageable
                );

        List<PostDto> postDtos = postMapper.toPostDtoList(posts.getContent());

        return new PostResponseVm(postDtos, posts.getTotalElements());

    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("post.not.found"));
        Long userId = authService.getCurrentUserId();
        if (post.getOwner() == null || !userId.equals(post.getOwner().getId())) {
            throw new RuntimeException("unauthorized.action");
        }
        post.setDeleted(true);
        postRepo.save(post);

        commentRepo.softDeleteByPostId(post.getId());
    }

    private static Pageable getPageable(int page, int size) {
        try {
            if (page < 1) {
                throw new RuntimeException("error.min.one.page");
            }
            return PageRequest.of(page - 1, size);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
