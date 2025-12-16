package com.errasoft.friendfinder.service;

import com.errasoft.friendfinder.controller.vm.MediaVM;
import com.errasoft.friendfinder.utils.MediaType;

import java.util.List;


public interface MediaService {
    List<MediaVM> getUserMedia(Long userId, MediaType type);
}
