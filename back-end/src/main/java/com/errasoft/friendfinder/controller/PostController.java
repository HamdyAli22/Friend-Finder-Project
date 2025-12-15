package com.errasoft.friendfinder.controller;

import com.errasoft.friendfinder.controller.vm.PostResponseVm;
import com.errasoft.friendfinder.dto.PostDto;
import com.errasoft.friendfinder.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create-post")
    public ResponseEntity<PostDto> createPost(@RequestBody @Valid PostDto postDto) {
        return ResponseEntity.ok(postService.createPost(postDto));
    }

    @PutMapping("/update-post")
    public ResponseEntity<PostDto> updatePost(@RequestBody @Valid PostDto postDto) {
        return ResponseEntity.ok(postService.updatePost(postDto));
    }

    @GetMapping("/feed")
    public ResponseEntity<PostResponseVm> feed(
            @RequestParam int page,
            @RequestParam int size) {
        return ResponseEntity.ok(postService.getFeed(page, size));
    }

    @GetMapping("/my-posts")
    public ResponseEntity<PostResponseVm> getMyPosts(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(postService.getMyPosts(page, size));
    }

    @GetMapping("/by-id")
    public ResponseEntity<PostDto> getPostById(@RequestParam Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @DeleteMapping("delete-post")
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}
