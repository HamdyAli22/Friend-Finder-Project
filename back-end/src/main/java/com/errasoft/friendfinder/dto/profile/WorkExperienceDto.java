package com.errasoft.friendfinder.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperienceDto {

    private Long id;

    private String companyName;

    private String jobTitle;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean currentJob;
}
