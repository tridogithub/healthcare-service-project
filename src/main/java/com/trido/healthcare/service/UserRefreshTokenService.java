package com.trido.healthcare.service;

import com.trido.healthcare.entity.token.UserRefreshToken;

import javax.validation.constraints.NotNull;

public interface UserRefreshTokenService {
    UserRefreshToken saveRefreshToken(@NotNull UserRefreshToken userRefreshToken);

    String getRefreshTokenByUsername(@NotNull String username);

    boolean checkValidAccessToken(String tokenId);

    void deleteUserToken(String username);
}
