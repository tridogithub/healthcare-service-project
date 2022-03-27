package com.trido.healthcare.controller;

import com.trido.healthcare.config.token.TokenUtils;
import com.trido.healthcare.constants.ConstantMessages;
import com.trido.healthcare.constants.Constants;
import com.trido.healthcare.domain.JwtResponse;
import com.trido.healthcare.entity.VerificationToken;
import com.trido.healthcare.entity.customuserdetails.MyUserDetails;
import com.trido.healthcare.service.UserRefreshTokenService;
import com.trido.healthcare.service.UserService;
import com.trido.healthcare.service.impl.MyUserDetailService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
@AllArgsConstructor
public class ValidationController {
    private final TokenUtils tokenUtils;
    private final MyUserDetailService myUserDetailService;
    private final UserRefreshTokenService userRefreshTokenService;
    private final UserService userService;

    @PostMapping(path = "/auth/token")
    public ResponseEntity<?> getAccessTokenFromRefreshToken(
            @RequestParam(name = Constants.REFRESH_TOKEN_PARAM) String refreshToken
    ) {
        Claims claims = tokenUtils.getClaimsFromJwtToken(refreshToken);
        String username = claims.getSubject();
        checkMatchedRefreshToken(username, refreshToken);

        MyUserDetails userDetails = (MyUserDetails) myUserDetailService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException(ConstantMessages.TOKEN_INVALID_USER_NAME);
        }
        JwtResponse jwtResponse = tokenUtils.createJwtResponse(userDetails);
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/registrationConfirm")
    @ResponseBody
    public String confirmRegistration(@RequestParam("token") String token) {
        VerificationToken verificationToken = userService.registrationTokenVerification(token);
        if (verificationToken != null) {
            return "Successfully Verification.";
        }
        return "Verification failed.";
    }

    private boolean checkMatchedRefreshToken(String username, String requestRefreshToken) {
        String savedRefreshToken = userRefreshTokenService.getRefreshTokenByUsername(username);
        if (!requestRefreshToken.equals(savedRefreshToken)) {
            throw new BadCredentialsException("Wrong refresh token");
        }
        return true;
    }
}
