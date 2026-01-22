package com.errasoft.friendfinder.service.impl.profile;

import com.errasoft.friendfinder.dto.profile.AboutProfileDto;
import com.errasoft.friendfinder.mapper.profile.AboutProfileMapper;
import com.errasoft.friendfinder.model.profile.AboutProfile;
import com.errasoft.friendfinder.model.profile.WorkExperience;
import com.errasoft.friendfinder.model.security.Account;
import com.errasoft.friendfinder.repo.profile.AboutProfileRepo;
import com.errasoft.friendfinder.service.profile.AboutProfileService;
import com.errasoft.friendfinder.service.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class AboutProfileServiceImpl implements AboutProfileService {

    private  AboutProfileRepo aboutProfileRepository;
    private  AboutProfileMapper aboutProfileMapper;
    private AuthService authService;


    @Autowired
    public AboutProfileServiceImpl(AboutProfileRepo aboutProfileRepository,
                                   AboutProfileMapper aboutProfileMapper,
                                   AuthService authService) {
        this.aboutProfileRepository = aboutProfileRepository;
        this.aboutProfileMapper = aboutProfileMapper;
        this.authService = authService;
    }

    @Override
    public AboutProfileDto createAboutProfile(AboutProfileDto aboutProfileDto) {

        if (Objects.nonNull(aboutProfileDto.getId())) {
            throw new RuntimeException("id.must_be.null");
        }

        Account currentAccount = authService.getCurrentAccount();

        boolean exists = aboutProfileRepository
                .existsByAccountAndDeletedFalse(currentAccount);

        if (exists) {
            throw new RuntimeException("about.profile.already.exists");
        }

        AboutProfile profile = aboutProfileMapper.toAboutProfile(aboutProfileDto);

        profile.setAccount(currentAccount);
        profile.setDeleted(false);

        AboutProfile saved = aboutProfileRepository.save(profile);

        return aboutProfileMapper.toAboutProfileDto(saved);
    }

    @Override
    public AboutProfileDto updateAboutProfile(AboutProfileDto aboutProfileDto) {

        if (aboutProfileDto.getId() == null) {
            throw new RuntimeException("id.must_be.not_null");
        }

        AboutProfile existing = aboutProfileRepository
                .findByIdAndDeletedFalse(aboutProfileDto.getId())
                .orElseThrow(() -> new RuntimeException("about.profile.not.found"));

        if (aboutProfileDto.getPersonalInfo() != null) {
            existing.setPersonalInfo(aboutProfileDto.getPersonalInfo());
        }

        if (aboutProfileDto.getAddress() != null) {
            existing.setAddress(aboutProfileDto.getAddress());
        }

        AboutProfile updated = aboutProfileRepository.save(existing);
        return aboutProfileMapper.toAboutProfileDto(updated);
    }

    @Override
    public AboutProfileDto getAboutProfileByAccountId(Long accountId) {
        AboutProfile profile = aboutProfileRepository
                .findByAccountIdAndDeletedFalse(accountId)
                .orElseThrow(() -> new RuntimeException("about.profile.not.found"));

        profile.setWorkExperiences(
                profile.getWorkExperiences().stream()
                        .filter(exp -> !exp.isDeleted())
                        .toList()
        );


        profile.setInterests(
                profile.getInterests().stream()
                        .filter(interest -> !interest.isDeleted())
                        .toList()
        );


        profile.setLanguages(
                profile.getLanguages().stream()
                        .filter(lang -> !lang.isDeleted())
                        .toList()
        );

        return aboutProfileMapper.toAboutProfileDto(profile);
    }

    @Override
    public void deleteAboutProfile(Long id) {

        AboutProfile profile = aboutProfileRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("about.profile.not.found"));


        profile.getWorkExperiences()
                .forEach(work -> work.setDeleted(true));


        profile.getInterests()
                .forEach(interest -> interest.setDeleted(true));


        profile.getLanguages()
                .forEach(language -> language.setDeleted(true));

        profile.setDeleted(true);
        aboutProfileRepository.save(profile);
    }

    @Override
    public void deletePersonalInfo(Long id) {
        AboutProfile profile = aboutProfileRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("about.profile.not.found"));

        profile.setPersonalInfo(null);
        aboutProfileRepository.save(profile);
    }

    @Override
    public void deleteAddress(Long id) {
        AboutProfile profile = aboutProfileRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("about.profile.not.found"));

        profile.setAddress(null);
        aboutProfileRepository.save(profile);

    }

}
