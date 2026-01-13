package com.errasoft.friendfinder.mapper.profile;

import com.errasoft.friendfinder.dto.profile.InterestDto;
import com.errasoft.friendfinder.model.profile.Interest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InterestMapper {

    Interest toInterest(InterestDto interestDto);

    InterestDto toInterestDto(Interest interest);

    List<Interest> toInterestList(List<InterestDto> interestDTOS);

    List<InterestDto> toInterestDtoList(List<Interest> interests);
}
