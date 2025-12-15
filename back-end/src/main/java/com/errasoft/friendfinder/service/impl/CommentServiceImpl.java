package com.errasoft.friendfinder.service.impl;

import com.errasoft.friendfinder.controller.vm.CommentResponseVm;
import com.errasoft.friendfinder.dto.CommentDto;
import com.errasoft.friendfinder.dto.security.AccountDto;
import com.errasoft.friendfinder.mapper.CommentMapper;
import com.errasoft.friendfinder.mapper.security.AccountMapper;
import com.errasoft.friendfinder.model.Comment;
import com.errasoft.friendfinder.model.Post;
import com.errasoft.friendfinder.model.security.Account;
import com.errasoft.friendfinder.repo.CommentRepo;
import com.errasoft.friendfinder.repo.PostRepo;
import com.errasoft.friendfinder.service.CommentService;
import com.errasoft.friendfinder.service.security.AuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepo commentRepo;
    private PostRepo postRepo;
    private CommentMapper commentMapper;
    private AuthService authService;
    private AccountMapper accountMapper;

    public CommentServiceImpl(CommentRepo commentRepo,
                              PostRepo postRepo,
                              CommentMapper commentMapper,
                              AccountMapper accountMapper,
                              AuthService authService) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.commentMapper = commentMapper;
        this.accountMapper = accountMapper;
        this.authService = authService;
    }

    @Override
    public CommentDto addComment(CommentDto CommentDto) {

        Post post = postRepo.findById(CommentDto.getPostId())
                .orElseThrow(() -> new RuntimeException("post.not.found"));

        Account owner = accountMapper.toAccount(authService.getCurrentUser());

        Comment comment = commentMapper.toComment(CommentDto);
        comment.setPost(post);
        comment.setOwner(owner);
        comment.setCreatedDate(LocalDateTime.now());

        Comment saved = commentRepo.save(comment);
        post.setCommentsCount(post.getCommentsCount() == null ? 1L : post.getCommentsCount() + 1);
        postRepo.save(post);

        return commentMapper.toCommentDto(saved);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("comment.not.found"));

        Long userId = authService.getCurrentUserId();

        if (comment.getOwner() == null || !userId.equals(comment.getOwner().getId())) {
            throw new RuntimeException("unauthorized.action");
        }

        comment.setDeleted(true);
        commentRepo.save(comment);

        Post post = comment.getPost();
        if (post != null && post.getCommentsCount() != null && post.getCommentsCount() > 0) {
            post.setCommentsCount(post.getCommentsCount() - 1);
            postRepo.save(post);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponseVm getCommentsByPost(Long postId, int page, int size) {

        postRepo.findById(postId).orElseThrow(() -> new RuntimeException("comment.not.found"));

        Pageable pageable = getPageable(page, size);

        Page<Comment> comments = commentRepo.findByPostIdAndDeletedFalseOrderByCreatedDateDesc(postId, pageable);
        List<CommentDto> commentDtos = commentMapper.toCommentDtoList(comments.getContent());

        return new CommentResponseVm(commentDtos, comments.getTotalElements());
    }

    @Override
    public CommentDto updateComment(CommentDto commentDto) {

        if (commentDto.getId() == null) {
            throw new RuntimeException("id.must.be.not.null");
        }

        postRepo.findByIdAndDeletedFalse(commentDto.getPostId())
                .orElseThrow(() -> new RuntimeException("post.not.found"));

        Comment comment = commentRepo.findByIdAndDeletedFalse(commentDto.getId())
                .orElseThrow(() -> new RuntimeException("comment.not.found"));

        Long userId = authService.getCurrentUserId();
        if (comment.getOwner() == null || !userId.equals(comment.getOwner().getId())) {
            throw new RuntimeException("unauthorized.action");
        }

        if (comment.isDeleted()) {
            throw new RuntimeException("comment.deleted");
        }

        if (commentDto.getContent() != null) {
            comment.setContent(commentDto.getContent());
        }

        comment.setUpdatedDate(LocalDateTime.now());

        Comment saved = commentRepo.save(comment);

        return commentMapper.toCommentDto(saved);
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
