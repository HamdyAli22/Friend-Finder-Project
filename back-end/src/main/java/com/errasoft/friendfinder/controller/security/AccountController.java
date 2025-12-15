package com.errasoft.friendfinder.controller.security;

import com.errasoft.friendfinder.controller.vm.ChangePassReqVM;
import com.errasoft.friendfinder.dto.security.AccountDetailsDto;
import com.errasoft.friendfinder.dto.security.AccountDto;
import com.errasoft.friendfinder.helper.BundleMessage;
import com.errasoft.friendfinder.service.bundlemessage.BundleMessageService;
import com.errasoft.friendfinder.service.security.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private AccountService accountService;
    private BundleMessageService bundleMessageService;

    public AccountController(AccountService accountService, BundleMessageService bundleMessageService) {
        this.accountService = accountService;
        this.bundleMessageService = bundleMessageService;
    }


    @PostMapping("/change-password")
    public ResponseEntity<BundleMessage> changePassword(@Valid @RequestBody ChangePassReqVM request) {
        String key = accountService.changePassword(request);
        BundleMessage message = bundleMessageService.getMessageArEn(key);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<BundleMessage> resetPassword(@Valid @RequestBody ChangePassReqVM request) {
        String key = accountService.resetPassword(request);
        BundleMessage message = bundleMessageService.getMessageArEn(key);
        return ResponseEntity.ok(message);
    }


    @GetMapping("/all-accounts")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PutMapping("/toggle-account")
    public ResponseEntity<BundleMessage> toggleAccountStatus(@RequestParam Long id) {
        String key = accountService.toggleAccountStatus(id);
        BundleMessage message = bundleMessageService.getMessageArEn(key);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/update-details")
    public ResponseEntity<AccountDto> updateAccountDetails(
            @RequestParam String email,
            @Valid @RequestBody AccountDetailsDto detailsDto) {
        AccountDto updatedAccount = accountService.updateAccountDetails(email, detailsDto);
        return ResponseEntity.ok(updatedAccount);
    }

}
