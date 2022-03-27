package com.trido.healthcare.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trido.healthcare.config.token.TokenUtils;
import com.trido.healthcare.entity.customuserdetails.MyUserDetails;
import com.trido.healthcare.domain.JwtResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private TokenUtils tokenUtils;

    @Autowired
    public MyAuthenticationSuccessHandler(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        JwtResponse jwtResponse = tokenUtils.createJwtResponse((MyUserDetails) authentication.getPrincipal());

        ObjectMapper objectMapper = new ObjectMapper();
        log.info(" Login successfully. ");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(jwtResponse));
    }
}
