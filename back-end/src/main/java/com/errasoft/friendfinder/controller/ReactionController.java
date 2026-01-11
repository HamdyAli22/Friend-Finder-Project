package com.errasoft.friendfinder.controller;

import com.errasoft.friendfinder.controller.vm.ReactionUsersVm;
import com.errasoft.friendfinder.service.ReactionService;
import com.errasoft.friendfinder.utils.ReactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reactions")

public class ReactionController {

    private ReactionService reactionService;

    @Autowired
    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @PostMapping("/react")
    public ResponseEntity<Void> react(
            @RequestParam Long postId,
            @RequestParam ReactionType type) {
        reactionService.react(postId, type);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-reaction")
    public ResponseEntity<ReactionType> getMyReaction(@RequestParam Long postId) {
        return ResponseEntity.ok(reactionService.getMyReaction(postId));
    }

    @GetMapping("/likes")
    public ResponseEntity<Long> countLikes(@RequestParam Long postId) {
        return ResponseEntity.ok(reactionService.countLikes(postId));
    }

    @GetMapping("/dislikes")
    public ResponseEntity<Long> countDislikes(@RequestParam Long postId) {
        return ResponseEntity.ok(reactionService.countDislikes(postId));
    }

    @GetMapping("/users")
    public ResponseEntity<ReactionUsersVm> getReactionUsers(@RequestParam Long postId) {
        return ResponseEntity.ok(reactionService.getPostReactionsUsers(postId));
    }

}
