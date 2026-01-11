package com.errasoft.friendfinder.service;

import com.errasoft.friendfinder.dto.ActivityDto;

import java.util.List;

public interface ActivityService {
    List<ActivityDto> getUserActivities(Long userId);
}
