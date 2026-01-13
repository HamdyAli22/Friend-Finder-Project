package com.errasoft.friendfinder.mapper.profile;

import com.errasoft.friendfinder.dto.profile.LanguageDto;
import com.errasoft.friendfinder.model.profile.Language;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LanguageMapper {

    Language toLanguage(LanguageDto languageDto);

    LanguageDto toLanguageDto(Language  language);

    List<Language> toLanguageList(List<LanguageDto> languageDtos);

    List<LanguageDto> toLanguageDtoList(List<Language> languages);
}

