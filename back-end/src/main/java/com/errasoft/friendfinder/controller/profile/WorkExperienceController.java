package com.errasoft.friendfinder.controller.profile;

import com.errasoft.friendfinder.dto.profile.WorkExperienceDto;
import com.errasoft.friendfinder.service.profile.WorkExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/about-profile/work-experience")
public class WorkExperienceController {

    private WorkExperienceService workExperienceService;

    @Autowired
    public WorkExperienceController(WorkExperienceService workExperienceService) {
        this.workExperienceService = workExperienceService;
    }


    @PostMapping("/create-work")
    public ResponseEntity<WorkExperienceDto> createWorkExperience(@RequestBody WorkExperienceDto dto) {
        return ResponseEntity.ok(workExperienceService.createWorkExperience(dto));
    }


    @PutMapping("/update-work")
    public ResponseEntity<WorkExperienceDto> updateWorkExperience(@RequestBody WorkExperienceDto dto) {
        return ResponseEntity.ok(workExperienceService.updateWorkExperience(dto));
    }


    @GetMapping("/get-work")
    public ResponseEntity<List<WorkExperienceDto>> getWorkExperiencesByAccountId(@RequestParam Long accountId) {
        return ResponseEntity.ok(workExperienceService.getWorkExperiencesByAccountId(accountId));
    }


    @DeleteMapping("/delete-work")
    public ResponseEntity<Void> deleteWorkExperience(@RequestParam (required = false) Long workExperienceId) {
        workExperienceService.deleteWorkExperience(workExperienceId);
        return ResponseEntity.noContent().build();
    }
}
