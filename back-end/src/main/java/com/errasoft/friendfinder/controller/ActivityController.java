package com.errasoft.friendfinder.controller;

import com.errasoft.friendfinder.dto.ActivityDto;
import com.errasoft.friendfinder.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    private ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }


    @GetMapping("/all-activities")
    public List<ActivityDto> getActivities(@RequestParam(required = false) Long userId) {
        return activityService.getUserActivities(userId);
    }
}
