package com.errasoft.friendfinder.service.impl;

import com.errasoft.friendfinder.controller.vm.MediaVM;
import com.errasoft.friendfinder.model.Post;
import com.errasoft.friendfinder.repo.PostRepo;
import com.errasoft.friendfinder.service.MediaService;
import com.errasoft.friendfinder.service.security.AuthService;
import com.errasoft.friendfinder.utils.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediaServiceImpl implements MediaService {

    private  PostRepo postRepo;
    private  AuthService authService;

    public MediaServiceImpl(PostRepo postRepo, AuthService authService) {
        this.postRepo = postRepo;
        this.authService = authService;
    }

    @Override
    public List<MediaVM> getMyMedia(MediaType type) {

        Long currentUserId = authService.getCurrentUserId();

        List<Post> posts;

        if (type != null) {
            posts = postRepo.findByOwnerIdAndMediaType(currentUserId, type);
        } else {
            posts = postRepo.findByOwnerId(currentUserId);
        }

        return posts.stream().map(p -> new MediaVM(
                p.getId(),
                p.getMediaType(),
                p.getMediaUrl()
        )).toList();
    }
}
