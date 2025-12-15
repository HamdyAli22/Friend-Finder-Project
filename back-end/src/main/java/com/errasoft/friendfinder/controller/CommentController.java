package com.errasoft.friendfinder.controller;

import com.errasoft.friendfinder.controller.vm.CommentResponseVm;
import com.errasoft.friendfinder.dto.CommentDto;
import com.errasoft.friendfinder.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create-comment")
    public ResponseEntity<CommentDto> addComment(@Valid @RequestBody CommentDto dto) {
        return ResponseEntity.ok(commentService.addComment(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<CommentResponseVm> getComments(
            @RequestParam Long postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId, page, size));
    }



    @DeleteMapping("/delete-comment")
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-comment")
    public ResponseEntity<CommentDto> updateComment(@Valid @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(commentService.updateComment(commentDto));
    }
}
