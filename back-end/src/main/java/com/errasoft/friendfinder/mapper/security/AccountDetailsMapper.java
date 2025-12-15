package com.errasoft.friendfinder.mapper.security;

import com.errasoft.friendfinder.dto.security.AccountDetailsDto;
import com.errasoft.friendfinder.model.security.AccountDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountDetailsMapper {

    @Mapping(target = "account", ignore = true)
    AccountDetails toAccountDetails (AccountDetailsDto  accountDetailsDto);

    @Mapping(target = "account", ignore = true)
    AccountDetailsDto toAccountDetailsDto (AccountDetails accountDetails);

    List<AccountDetails> toAccountDetailsList (List<AccountDetailsDto>  accountDetailsDto);

    List<AccountDetailsDto> toAccountDetailsDtoList (List<AccountDetails> accountDetails);
}
