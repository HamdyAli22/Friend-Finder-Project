package com.errasoft.friendfinder.service.security;

import com.errasoft.friendfinder.controller.vm.ChangePassReqVM;
import com.errasoft.friendfinder.dto.security.AccountDetailsDto;
import com.errasoft.friendfinder.dto.security.AccountDto;

import java.util.List;

public interface AccountService {
    AccountDto getByEmail(String email);
    AccountDto createAccount(AccountDto accountDto);
    String changePassword(ChangePassReqVM request);
    String resetPassword(ChangePassReqVM request);
    List<AccountDto> getAllAccounts();
    String toggleAccountStatus(Long id);
    AccountDto updateAccountDetails(AccountDetailsDto accountDetailsDto);
    AccountDto updateBasicInfo(AccountDto accountDto);
}
