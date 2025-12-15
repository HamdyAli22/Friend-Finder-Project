package com.errasoft.friendfinder.service;

import com.errasoft.friendfinder.controller.vm.CommentResponseVm;
import com.errasoft.friendfinder.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(CommentDto dto);
    void deleteComment(Long commentId);
    CommentResponseVm getCommentsByPost(Long postId, int page, int size);
    CommentDto updateComment(CommentDto commentDto);

}
