package com.errasoft.friendfinder.controller;

import com.errasoft.friendfinder.controller.vm.MediaVM;
import com.errasoft.friendfinder.service.MediaService;
import com.errasoft.friendfinder.utils.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/media")
public class MediaController {

    private MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping("/user-media")
    public ResponseEntity<List<MediaVM>> getUserMedia(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) MediaType type
    ) {
        return ResponseEntity.ok(mediaService.getUserMedia(userId, type));
    }
}
