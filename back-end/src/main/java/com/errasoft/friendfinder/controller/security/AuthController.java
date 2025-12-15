
package com.errasoft.friendfinder.controller.security;

import com.errasoft.friendfinder.controller.vm.AuthRequestVm;
import com.errasoft.friendfinder.controller.vm.AuthResponseVm;
import com.errasoft.friendfinder.dto.security.AccountDto;
import com.errasoft.friendfinder.service.security.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    ResponseEntity<AuthResponseVm> login(@RequestBody @Valid AuthRequestVm authRequestVm){
        return ResponseEntity.ok(authService.login(authRequestVm));
    }

    @PostMapping("/signup")
    ResponseEntity<AuthResponseVm> signup(@RequestBody @Valid AccountDto  accountDto){
        return ResponseEntity.ok(authService.signup(accountDto));
    }
}
