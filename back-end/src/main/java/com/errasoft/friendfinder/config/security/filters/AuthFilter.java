
package com.errasoft.friendfinder.config.security.filters;

import com.errasoft.friendfinder.config.security.TokenHandler;
import com.errasoft.friendfinder.dto.security.AccountDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private TokenHandler tokenHandler;


    @Autowired
    public AuthFilter(TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String token =  request.getHeader("Authorization");
      if(token == null || !token.startsWith("Bearer"))
      {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return;
      }

      token = token.substring(7);
      AccountDto accountDto = tokenHandler.validateToken(token);
      if(Objects.isNull(accountDto)){
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return;
      }

        List<SimpleGrantedAuthority> roles = getAuthorities(accountDto);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                 new UsernamePasswordAuthenticationToken(accountDto, accountDto.getPassword(), roles);

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(request,response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (request.getRequestURI().contains("login") ||
                request.getRequestURI().contains("signup") ||
                request.getRequestURI().contains("reset-password") ||request.getRequestURI().contains("/uploads/")) {
            return true;
        }
        return false;
    }

    private List<SimpleGrantedAuthority> getAuthorities(AccountDto accountDto){
        return accountDto.getRoles().stream().map(roleDto -> new SimpleGrantedAuthority("ROLE_" + roleDto.getRoleName()))
                .collect(Collectors.toList());
    }


}
