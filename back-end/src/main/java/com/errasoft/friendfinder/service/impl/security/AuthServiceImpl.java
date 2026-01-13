
package com.errasoft.friendfinder.service.impl.security;

import com.errasoft.friendfinder.config.security.TokenHandler;
import com.errasoft.friendfinder.controller.vm.AuthRequestVm;
import com.errasoft.friendfinder.controller.vm.AuthResponseVm;
import com.errasoft.friendfinder.dto.security.AccountDto;
import com.errasoft.friendfinder.dto.security.RoleDto;
import com.errasoft.friendfinder.mapper.security.AccountMapper;
import com.errasoft.friendfinder.model.security.Account;
import com.errasoft.friendfinder.repo.FriendshipRepo;
import com.errasoft.friendfinder.repo.security.AccountRepo;
import com.errasoft.friendfinder.service.security.AccountService;
import com.errasoft.friendfinder.service.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private AccountService accountService;
    private TokenHandler tokenHandler;
    private PasswordEncoder passwordEncoder;
    private AccountRepo accountRepo;
    private AccountMapper accountMapper;
    private FriendshipRepo friendshipRepo;

    @Autowired
    public AuthServiceImpl(AccountService accountService,
                           TokenHandler tokenHandler,
                           PasswordEncoder passwordEncoder,
                           AccountRepo accountRepo,
                           AccountMapper accountMapper,
                           FriendshipRepo friendshipRepo) {
        this.accountService = accountService;
        this.tokenHandler = tokenHandler;
        this.passwordEncoder = passwordEncoder;
        this.accountRepo = accountRepo;
        this.accountMapper = accountMapper;
        this.friendshipRepo = friendshipRepo;
    }


    @Override
    public AuthResponseVm login(AuthRequestVm authRequestVm) {
        AccountDto accountDto = accountService.getByEmail(authRequestVm.getEmail());

        if (!accountDto.isEnabled()) {
            throw new RuntimeException("account.disabled");
        }

        if(!passwordEncoder.matches(authRequestVm.getPassword(), accountDto.getPassword())) {
            throw new RuntimeException("auth.invalid.password");
        }

        AuthResponseVm authResponseVm = new AuthResponseVm();
        authResponseVm.setUsername(accountDto.getUsername());
        authResponseVm.setEmail(accountDto.getEmail());
        authResponseVm.setToken(tokenHandler.createToken(accountDto));
        authResponseVm.setUserRoles(getAccountRoles(accountDto));
        authResponseVm.setUserId(accountDto.getId());

        return authResponseVm;
    }

    @Override
    public AuthResponseVm signup(AccountDto accountDto) {

        AccountDto savedAccountDto =  accountService.createAccount(accountDto);
        if(Objects.isNull(savedAccountDto)){
            throw new RuntimeException("account.not.created");
        }

        AuthResponseVm authResponseVm = new AuthResponseVm();
        authResponseVm.setUsername(savedAccountDto.getUsername());
        authResponseVm.setEmail(savedAccountDto.getEmail());
        authResponseVm.setToken(tokenHandler.createToken(savedAccountDto));
        authResponseVm.setUserRoles(getAccountRoles(savedAccountDto));
        authResponseVm.setUserId(savedAccountDto.getId());

        return authResponseVm;
    }

    @Override
    public Long getCurrentUserId() {
        AccountDto accountDto = getCurrentUser();
        return accountDto != null ? accountDto.getId() : null;
    }

    @Override
    public AccountDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDto accountDto = (AccountDto)authentication.getPrincipal();
        return accountDto;
    }

    @Override
    public Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDto accountDto = (AccountDto)authentication.getPrincipal();
        return accountMapper.toAccount(accountDto);
    }

    @Override
    public AccountDto getUserById(Long userId) {

        Long id = userId != null ? userId : getCurrentUserId();
        Account account = accountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("account.not.found"));

        AccountDto dto = accountMapper.toAccountDto(account);

        int friendsCount = friendshipRepo.findAcceptedFriendIds(id).size();
        dto.setFriendsCount(friendsCount);

        return dto;
    }

    @Override
    public String getCurrentUserEmail() {
        AccountDto accountDto = getCurrentUser();
        return accountDto != null ? accountDto.getEmail() : null;
    }

    private List<String> getAccountRoles(AccountDto accountDto) {
        return accountDto.getRoles().stream().map(RoleDto::getRoleName).collect(Collectors.toList());
    }

}
