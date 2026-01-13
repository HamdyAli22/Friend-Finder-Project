package com.errasoft.friendfinder.dto.profile;

import com.errasoft.friendfinder.dto.security.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AboutProfileDto {

    private Long id;

    private String personalInfo;

    private String address;

    private AccountDto account;

    private List<WorkExperienceDto> workExperiences;

    private List<InterestDto> interests;

    private List<LanguageDto> languages;
}
