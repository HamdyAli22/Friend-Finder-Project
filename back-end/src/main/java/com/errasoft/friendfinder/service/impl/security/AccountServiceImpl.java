package com.errasoft.friendfinder.service.impl.security;

import com.errasoft.friendfinder.controller.vm.ChangePassReqVM;
import com.errasoft.friendfinder.dto.security.AccountDetailsDto;
import com.errasoft.friendfinder.dto.security.AccountDto;
import com.errasoft.friendfinder.mapper.security.AccountDetailsMapper;
import com.errasoft.friendfinder.mapper.security.AccountMapper;
import com.errasoft.friendfinder.model.security.Account;
import com.errasoft.friendfinder.model.security.AccountDetails;
import com.errasoft.friendfinder.model.security.Role;
import com.errasoft.friendfinder.repo.security.AccountRepo;
import com.errasoft.friendfinder.repo.security.RoleRepo;
import com.errasoft.friendfinder.service.security.AccountService;
import com.errasoft.friendfinder.service.security.AuthService;
import com.errasoft.friendfinder.utils.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepo accountRepo;
    private AccountMapper accountMapper;
    private AccountDetailsMapper detailsMapper;
    private PasswordEncoder passwordEncoder;
    private RoleRepo roleRepo;
    private AuthService authService;

    @Autowired
    public AccountServiceImpl(AccountMapper accountMapper,
                              AccountDetailsMapper detailsMapper,
                              AccountRepo accountRepo,
                              RoleRepo roleRepo,
                              @Lazy PasswordEncoder passwordEncoder,
                              @Lazy AuthService authService) {
        this.accountMapper = accountMapper;
        this.accountRepo = accountRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.detailsMapper = detailsMapper;
        this.authService = authService;
    }

    @Override
    public AccountDto getByEmail(String email) {
        Optional<Account>  account = accountRepo.findByEmail(email);
        if(!account.isPresent()){
            throw new RuntimeException("account.username.notExists");
        }
        return accountMapper.toAccountDto(account.get());
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {

      if(Objects.nonNull(accountDto.getId())){
        throw new RuntimeException("id.must_be.null");
      }

      if(accountRepo.findByEmail(accountDto.getEmail()).isPresent()){
          throw new RuntimeException("account.username.exists");
      }

        Account account = accountMapper.toAccount(accountDto);

        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));

        account.setEnabled(true);

        initRoleToAccount(account);

        if (account.getAccountDetails() != null) {
            account.getAccountDetails().setAccount(account);
        }
        System.out.println(">>> About to save account: " + account);
        System.out.println(">>> Account roles: " + account.getRoles());
        System.out.println(">>> AccountDetails: " + account.getAccountDetails());

        Account savedAccount = accountRepo.save(account);
        return accountMapper.toAccountDto(savedAccount);
    }

    @Override
    public String changePassword(ChangePassReqVM request) {

        AccountDto accountDto = (AccountDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountMapper.toAccount(accountDto);

        if (account == null || account.getUsername() == null) {
            throw new RuntimeException("account.user.unauthorized");
        }

        validateChangePasswordRequest(request, false, account);

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepo.save(account);

        SecurityContextHolder.clearContext();

        return "password.changed.success";
    }

    @Override
    public String resetPassword(ChangePassReqVM request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new RuntimeException("not_empty.email");
        }

        Account account = accountRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("account.username.notExists"));

        validateChangePasswordRequest(request, true, account);

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepo.save(account);

        return "password.changed.success";
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepo.findAll();
        List<AccountDto> accountDtos = accountMapper.toAccountDtoList(accounts);
        return accountDtos;
    }

    @Override
    public String toggleAccountStatus(Long id) {
        Account account = accountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("account.username.notExists"));
        account.setEnabled(!account.isEnabled());
        accountRepo.save(account);
        return account.isEnabled() ? "account.enabled.success" : "account.disabled.success";
    }

    @Override
    public AccountDto updateAccountDetails(AccountDetailsDto accountDetailsDto) {

        Account account = authService.getCurrentAccount();

        AccountDetails accountDetails = account.getAccountDetails();

        if (accountDetails != null) {
            accountDetails.setAddress(accountDetailsDto.getAddress());
            accountDetails.setAge(accountDetailsDto.getAge());
            accountDetails.setPhoneNumber(accountDetailsDto.getPhoneNumber());
        }else{
            accountDetails = detailsMapper.toAccountDetails(accountDetailsDto);
            accountDetails.setAccount(account);
            account.setAccountDetails(accountDetails);
        }

        accountRepo.save(account);
        return accountMapper.toAccountDto(account);
    }

    @Override
    public AccountDto updateBasicInfo(AccountDto accountDto) {

        Account account = authService.getCurrentAccount();

        AccountDetails details = account.getAccountDetails();
        if (details != null) {
            details.setAccount(account);
        }

        if (accountDto.getUsername() != null && !accountDto.getUsername().isEmpty()) {
            account.setUsername(accountDto.getUsername());
        }

        if (accountDto.getBio() != null) {
            account.setBio(accountDto.getBio());
        }

        account.setProfileImageUrl(accountDto.getProfileImageUrl());
        account.setCoverImageUrl(accountDto.getCoverImageUrl());


        Account savedAccount = accountRepo.save(account);
        return accountMapper.toAccountDto(savedAccount);
    }

    private void validateChangePasswordRequest(ChangePassReqVM request, boolean isForgotMode, Account account) {

        if (!isForgotMode) {
            if (request.getOldPassword() == null || request.getOldPassword().isEmpty()) {
                throw new RuntimeException("old.pass.required");
            }

            if (!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
                throw new RuntimeException("old.password.notCorrect");
            }
        }

        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            throw new RuntimeException("new.pass.required");
        }

        if (!request.getNewPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{7,}$")) {
            throw new RuntimeException("new.pass.regexp");
        }

        if (!isForgotMode && passwordEncoder.matches(request.getNewPassword(), account.getPassword())) {
            throw new RuntimeException("password.sameAsOld");
        }

        if (request.getConfirmPassword() == null || request.getConfirmPassword().isEmpty()) {
            throw new RuntimeException("confirm.pass.required");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("passwords.notMatch");
        }
    }

        private void initRoleToAccount(Account account){
      Role role = roleRepo.findByRoleName(RoleEnum.USER.toString()).
              orElseThrow(() -> new RuntimeException("role.notFound"));
      List<Role> roles = account.getRoles();
      if (Objects.isNull(roles)) {
            roles = new ArrayList<>();
        }
        roles.add(role);
        account.setRoles(roles);
    }
}
