package com.trido.healthcare.controller;

import com.trido.healthcare.config.token.TokenUtils;
import com.trido.healthcare.constants.ConstantMessages;
import com.trido.healthcare.constants.Constants;
import com.trido.healthcare.controller.dto.JwtResponse;
import com.trido.healthcare.entity.MyUserDetails;
import com.trido.healthcare.service.MyUserDetailService;
import com.trido.healthcare.service.UserRefreshTokenService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ValidationController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private UserRefreshTokenService userRefreshTokenService;

    @PostMapping(path = "/auth/token")
    public ResponseEntity getAccessTokenFromRefreshToken(
            @RequestParam(name = Constants.REFRESH_TOKEN_PARAM) String refreshToken
    ) {
        Claims claims = tokenUtils.getClaimsFromJwtToken(refreshToken);
        String username = claims.getSubject();
        checkMatchedRefreshToken(username, refreshToken);

        MyUserDetails userDetails = (MyUserDetails) myUserDetailService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException(ConstantMessages.INVALID_USERNAME);
        }
        JwtResponse jwtResponse = tokenUtils.createJwtResponse(userDetails);
        return new ResponseEntity(jwtResponse, HttpStatus.OK);
    }

    private boolean checkMatchedRefreshToken(String username, String requestRefreshToken) {
        String savedRefreshToken = userRefreshTokenService.getRefreshTokenByUsername(username);
        if (savedRefreshToken == null || !requestRefreshToken.equals(savedRefreshToken)) {
            throw new BadCredentialsException("Wrong refresh token");
        }
        return true;
    }
}
