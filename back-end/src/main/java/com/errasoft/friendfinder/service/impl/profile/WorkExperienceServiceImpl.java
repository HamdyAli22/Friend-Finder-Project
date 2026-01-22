package com.errasoft.friendfinder.service.impl.profile;

import com.errasoft.friendfinder.dto.profile.WorkExperienceDto;
import com.errasoft.friendfinder.mapper.profile.WorkExperienceMapper;
import com.errasoft.friendfinder.model.profile.AboutProfile;
import com.errasoft.friendfinder.model.profile.WorkExperience;
import com.errasoft.friendfinder.repo.profile.AboutProfileRepo;
import com.errasoft.friendfinder.repo.profile.WorkExperienceRepo;
import com.errasoft.friendfinder.service.profile.WorkExperienceService;
import com.errasoft.friendfinder.service.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class WorkExperienceServiceImpl implements WorkExperienceService {

    private  WorkExperienceRepo workExperienceRepo;
    private  AboutProfileRepo aboutProfileRepo;
    private  WorkExperienceMapper workExperienceMapper;
    private AuthService  authService;

    @Autowired
    public WorkExperienceServiceImpl(WorkExperienceRepo workExperienceRepo,
                                     AboutProfileRepo aboutProfileRepo,
                                     WorkExperienceMapper workExperienceMapper,
                                     AuthService authService) {

        this.workExperienceRepo = workExperienceRepo;
        this.aboutProfileRepo = aboutProfileRepo;
        this.workExperienceMapper = workExperienceMapper;
        this.authService = authService;
    }

    @Override
    public WorkExperienceDto createWorkExperience(WorkExperienceDto workExperienceDto) {

        if (Objects.nonNull(workExperienceDto.getId())) {
            throw new RuntimeException("id.must_be.null");
        }

        Long accountId = authService.getCurrentUserId();

        AboutProfile aboutProfile = aboutProfileRepo
                .findByAccountIdAndDeletedFalse(accountId)
                .orElseThrow(() -> new RuntimeException("about.profile.not.found"));

        WorkExperience workExperience = workExperienceMapper.toWorkExperience(workExperienceDto);
        workExperience.setAboutProfile(aboutProfile);
        workExperience.setDeleted(false);

        WorkExperience saved = workExperienceRepo.save(workExperience);
        return workExperienceMapper.toWorkExperienceDto(saved);
    }

    @Override
    public WorkExperienceDto updateWorkExperience(WorkExperienceDto experienceDto) {

        if (experienceDto.getId() == null) {
            throw new RuntimeException("id.must_be.not_null");
        }

        WorkExperience existing = workExperienceRepo
                .findByIdAndDeletedFalse(experienceDto.getId())
                .orElseThrow(() -> new RuntimeException("work.experience.not.found"));

        if (experienceDto.getCompanyName() != null) {
            existing.setCompanyName(experienceDto.getCompanyName());
        }

        if (experienceDto.getJobTitle() != null) {
            existing.setJobTitle(experienceDto.getJobTitle());
        }

        if (experienceDto.getStartDate() != null) {
            existing.setStartDate(experienceDto.getStartDate());
        }

        if (experienceDto.getEndDate() != null) {
            existing.setEndDate(experienceDto.getEndDate());
        }

       existing.setCurrentJob(experienceDto.isCurrentJob());

        WorkExperience updated = workExperienceRepo.save(existing);
        return workExperienceMapper.toWorkExperienceDto(updated);
    }

    @Override
    public void deleteWorkExperience(Long id) {

        Long accountId = authService.getCurrentUserId();

        if(id != null){

            WorkExperience existing = workExperienceRepo
                    .findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new RuntimeException("work.experience.not.found"));

            if (!existing.getAboutProfile().getAccount().getId().equals(accountId)) {
                throw new RuntimeException("account.user.denied");
            }

            existing.setDeleted(true);
            workExperienceRepo.save(existing);
        } else{
            List<WorkExperience> experiences =
                    workExperienceRepo.findByAboutProfileAccountIdAndDeletedFalse(accountId);

            if (experiences.isEmpty()) {
                return;
            }

            experiences.forEach(exp -> exp.setDeleted(true));
            workExperienceRepo.saveAll(experiences);
        }


    }

    @Override
    public List<WorkExperienceDto> getWorkExperiencesByAccountId(Long accountId) {

        List<WorkExperience> experiences = workExperienceRepo
                .findByAboutProfileAccountIdAndDeletedFalse(accountId);

        if (experiences.isEmpty()) {
            throw new RuntimeException("work.experience.not.found");
        }

        return experiences.stream()
                .map(workExperienceMapper::toWorkExperienceDto)
                .toList();
    }
}
