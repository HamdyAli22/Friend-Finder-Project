package com.errasoft.friendfinder.service.profile;

import com.errasoft.friendfinder.dto.profile.WorkExperienceDto;

import java.util.List;

public interface WorkExperienceService {

    WorkExperienceDto createWorkExperience(WorkExperienceDto dto);

    WorkExperienceDto updateWorkExperience(WorkExperienceDto dto);

    void deleteWorkExperience(Long id);

    List<WorkExperienceDto> getWorkExperiencesByAccountId(Long accountId);
}
