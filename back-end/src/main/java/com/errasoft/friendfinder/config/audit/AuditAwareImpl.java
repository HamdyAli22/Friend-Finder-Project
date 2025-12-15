package com.errasoft.friendfinder.config.audit;

import com.errasoft.friendfinder.dto.security.AccountDto;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDto accountDto = (AccountDto) authentication.getPrincipal();
        return Optional.of(accountDto.getUsername());
    }
}
