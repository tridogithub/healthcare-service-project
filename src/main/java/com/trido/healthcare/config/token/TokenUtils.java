package com.trido.healthcare.config.token;

import com.trido.healthcare.controller.dto.JwtResponse;
import com.trido.healthcare.entity.MyUserDetails;
import io.jsonwebtoken.Claims;

public interface TokenUtils {
    Claims getClaimsFromJwtToken(String jwtToken);

    String generateJwtToken(Claims claims);

    JwtResponse createJwtResponse(MyUserDetails user);

    Claims checkValidAccessToken(String accessToken);
}
