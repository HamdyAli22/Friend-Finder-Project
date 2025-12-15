package com.errasoft.friendfinder.service;

import com.errasoft.friendfinder.controller.vm.MediaVM;
import com.errasoft.friendfinder.utils.MediaType;

import java.util.List;


public interface MediaService {
    List<MediaVM> getMyMedia(MediaType type);
}
