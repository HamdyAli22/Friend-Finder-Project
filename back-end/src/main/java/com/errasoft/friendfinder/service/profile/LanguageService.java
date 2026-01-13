package com.errasoft.friendfinder.service.profile;

import com.errasoft.friendfinder.dto.profile.LanguageDto;

import java.util.List;

public interface LanguageService {
    LanguageDto createLanguage(LanguageDto LanguageDto);

    LanguageDto updateLanguage(LanguageDto LanguageDto);

    List<LanguageDto> getLanguagesByAccountId(Long accountId);

    void deleteLanguage(Long id);
}
