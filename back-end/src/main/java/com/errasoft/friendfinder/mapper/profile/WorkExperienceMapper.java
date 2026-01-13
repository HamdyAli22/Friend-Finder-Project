package com.errasoft.friendfinder.mapper.profile;

import com.errasoft.friendfinder.dto.profile.WorkExperienceDto;
import com.errasoft.friendfinder.model.profile.WorkExperience;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkExperienceMapper {

    WorkExperience toWorkExperience(WorkExperienceDto dto);

    WorkExperienceDto toWorkExperienceDto(WorkExperience workExperience);

    List<WorkExperience> toWorkExperienceList(List<WorkExperienceDto> dtoList);

    List<WorkExperienceDto> toWorkExperienceDtoList(List<WorkExperience> workExperiences);


}
