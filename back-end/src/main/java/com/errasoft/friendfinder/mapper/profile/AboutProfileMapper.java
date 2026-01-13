package com.errasoft.friendfinder.mapper.profile;

import com.errasoft.friendfinder.dto.profile.AboutProfileDto;
import com.errasoft.friendfinder.mapper.security.AccountMapper;
import com.errasoft.friendfinder.model.profile.AboutProfile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, WorkExperienceMapper.class, InterestMapper.class, LanguageMapper.class})
public interface AboutProfileMapper {

    AboutProfile toAboutProfile(AboutProfileDto dto);

    AboutProfileDto toAboutProfileDto(AboutProfile aboutProfile);

    List<AboutProfile> toAboutProfileList(List<AboutProfileDto> dtoList);

    List<AboutProfileDto> toAboutProfileDtoList(List<AboutProfile> aboutProfiles);

}
