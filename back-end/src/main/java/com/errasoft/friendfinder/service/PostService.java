package com.errasoft.friendfinder.service;

import com.errasoft.friendfinder.controller.vm.PostResponseVm;
import com.errasoft.friendfinder.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    PostDto updatePost(PostDto postDto);

    PostResponseVm getFeed(int page, int size);

    PostDto getPostById(Long id);

    PostResponseVm getMyPosts(int page, int size);

    PostResponseVm getUserPosts(int page, int size,int userId);

    void deletePost(Long id);
}
