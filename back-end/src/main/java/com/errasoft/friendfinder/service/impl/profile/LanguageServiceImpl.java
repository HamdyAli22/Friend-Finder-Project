package com.errasoft.friendfinder.service.impl.profile;

import com.errasoft.friendfinder.dto.profile.LanguageDto;
import com.errasoft.friendfinder.mapper.profile.LanguageMapper;
import com.errasoft.friendfinder.model.profile.AboutProfile;
import com.errasoft.friendfinder.model.profile.Language;
import com.errasoft.friendfinder.repo.profile.AboutProfileRepo;
import com.errasoft.friendfinder.repo.profile.LanguageRepo;
import com.errasoft.friendfinder.service.profile.LanguageService;
import com.errasoft.friendfinder.service.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LanguageServiceImpl implements LanguageService {

    private LanguageRepo languageRepo;
    private AboutProfileRepo aboutProfileRepo;
    private LanguageMapper languageMapper;
    private AuthService authService;

    @Autowired
    public LanguageServiceImpl(LanguageRepo languageRepo,
                               AboutProfileRepo aboutProfileRepo,
                               LanguageMapper languageMapper,
                               AuthService authService) {
        this.languageRepo = languageRepo;
        this.aboutProfileRepo = aboutProfileRepo;
        this.languageMapper = languageMapper;
        this.authService = authService;
    }


    @Override
    public LanguageDto createLanguage(LanguageDto LanguageDto) {

        if (Objects.nonNull(LanguageDto.getId())) {
            throw new RuntimeException("id.must_be.null");
        }

        Long accountId = authService.getCurrentUserId();

        AboutProfile aboutProfile = aboutProfileRepo
                .findByAccountIdAndDeletedFalse(accountId)
                .orElseThrow(() -> new RuntimeException("about.profile.not.found"));

        Language language = languageMapper.toLanguage(LanguageDto);
        language.setAboutProfile(aboutProfile);
        language.setDeleted(false);

        Language saved = languageRepo.save(language);
        return languageMapper.toLanguageDto(saved);
    }

    @Override
    public LanguageDto updateLanguage(LanguageDto LanguageDto) {

        if (LanguageDto.getId() == null) {
            throw new RuntimeException("id.must_be.not_null");
        }

        Language existing = languageRepo
                .findByIdAndDeletedFalse(LanguageDto.getId())
                .orElseThrow(() -> new RuntimeException("language.not.found"));

        if (LanguageDto.getName() != null) {
            existing.setName(LanguageDto.getName());
        }

        Language updated = languageRepo.save(existing);
        return languageMapper.toLanguageDto(updated);
    }

    @Override
    public List<LanguageDto> getLanguagesByAccountId(Long accountId) {
        List<Language> languages = languageRepo
                .findByAboutProfileAccountIdAndDeletedFalse(accountId);

        if (languages.isEmpty()) {
            throw new RuntimeException("language.not.found");
        }

        return languages.stream()
                .map(languageMapper::toLanguageDto)
                .toList();
    }

    @Override
    public void deleteLanguage(Long id) {
        Long accountId = authService.getCurrentUserId();

        if (id != null) {
            Language language = languageRepo
                    .findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new RuntimeException("language.not.found"));

            if (!language.getAboutProfile().getAccount().getId().equals(accountId)) {
                throw new RuntimeException("account.user.denied");
            }

            language.setDeleted(true);
            languageRepo.save(language);

        } else {
            List<Language> languages =
                    languageRepo.findByAboutProfileAccountIdAndDeletedFalse(accountId);

            if (languages.isEmpty()) {
                return;
            }

            languages.forEach(l -> l.setDeleted(true));
            languageRepo.saveAll(languages);
        }
    }
}
