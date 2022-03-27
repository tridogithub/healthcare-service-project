package com.trido.healthcare.config.token;

import com.trido.healthcare.domain.JwtResponse;
import com.trido.healthcare.entity.customuserdetails.MyUserDetails;
import io.jsonwebtoken.Claims;

public interface TokenUtils {
    Claims getClaimsFromJwtToken(String jwtToken);

    String generateJwtToken(Claims claims);

    JwtResponse createJwtResponse(MyUserDetails user);

    Claims checkValidAccessToken(String accessToken);
}
