package com.errasoft.friendfinder.mapper;

import com.errasoft.friendfinder.dto.ContactInfoDto;
import com.errasoft.friendfinder.mapper.security.AccountMapper;
import com.errasoft.friendfinder.model.ContactInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AccountMapper.class})
public interface ContactInfoMapper {


    ContactInfoDto toContactInfoDto(ContactInfo contactInfo);

    @Mapping(target = "account.contacts", ignore = true)
    ContactInfo toContactInfo(ContactInfoDto contactInfoDto);

    List<ContactInfoDto> toContactInfoDtoList(List<ContactInfo> contactInfos);

    List<ContactInfo> toContactInfoList(List<ContactInfoDto> contactInfoDtos);
}
