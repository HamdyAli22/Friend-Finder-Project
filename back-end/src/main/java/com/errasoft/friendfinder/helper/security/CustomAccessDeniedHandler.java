
package com.errasoft.friendfinder.helper.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.errasoft.friendfinder.controller.vm.ExceptionResponse;
import com.errasoft.friendfinder.service.bundlemessage.BundleMessageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private BundleMessageService bundleMessageService;

    public CustomAccessDeniedHandler(BundleMessageService bundleMessageService) {
        this.bundleMessageService = bundleMessageService;
    }


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String message = bundleMessageService.getMessage("account.user.denied");
        ExceptionResponse body = new ExceptionResponse(message);

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(body));

    }
}
