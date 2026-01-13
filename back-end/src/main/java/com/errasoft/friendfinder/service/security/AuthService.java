package com.errasoft.friendfinder.service.security;

import com.errasoft.friendfinder.controller.vm.AuthRequestVm;
import com.errasoft.friendfinder.controller.vm.AuthResponseVm;
import com.errasoft.friendfinder.dto.security.AccountDto;
import com.errasoft.friendfinder.model.security.Account;

public interface AuthService {

    AuthResponseVm login(AuthRequestVm authRequestVm);
    AuthResponseVm signup(AccountDto accountDto);
    Long getCurrentUserId();
    AccountDto getCurrentUser();
    Account getCurrentAccount();
    AccountDto getUserById(Long id);
    String getCurrentUserEmail();
}
