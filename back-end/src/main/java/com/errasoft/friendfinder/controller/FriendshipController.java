package com.errasoft.friendfinder.controller;

import com.errasoft.friendfinder.controller.vm.FriendResponseVM;
import com.errasoft.friendfinder.dto.FriendshipDto;
import com.errasoft.friendfinder.model.security.Account;
import com.errasoft.friendfinder.service.FriendshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friendships")
public class FriendshipController {
    private FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @PostMapping("/send-request")
    public ResponseEntity<FriendshipDto> sendRequest(@RequestParam Long receiverId) {
        return ResponseEntity.ok(friendshipService.sendRequest(receiverId));
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> accept(@RequestParam Long friendshipId) {
        friendshipService.acceptRequest(friendshipId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<Void> reject(@RequestParam Long friendshipId) {
        friendshipService.rejectRequest(friendshipId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancel(@RequestParam Long friendshipId) {
        friendshipService.cancelRequest(friendshipId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-friends")
    public ResponseEntity<List<FriendResponseVM>> friends() {
        return ResponseEntity.ok(friendshipService.getMyFriends());
    }

    @GetMapping("/pending/received")
    public ResponseEntity<List<FriendResponseVM>> pendingReceived() {
        return ResponseEntity.ok(friendshipService.getPendingReceivedRequests());
    }

    @GetMapping("/pending/sent")
    public ResponseEntity<List<FriendResponseVM>> pendingSent() {
        return ResponseEntity.ok(friendshipService.getPendingSentRequests());
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<FriendResponseVM>> suggestions() {
        return ResponseEntity.ok(friendshipService.getSuggestions());
    }

}
