package com.trido.healthcare.service.impl;

import com.trido.healthcare.entity.token.UserRefreshToken;
import com.trido.healthcare.repository.UserRefreshTokenRepository;
import com.trido.healthcare.service.UserRefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserRefreshTokenServiceImpl implements UserRefreshTokenService {
    @Autowired
    private UserRefreshTokenRepository userRefreshTokenRepository;

    public UserRefreshToken saveRefreshToken(@NotNull UserRefreshToken userRefreshToken) {
        Optional<UserRefreshToken> existingUserRefreshToken =
                userRefreshTokenRepository.findById(userRefreshToken.getUsername());
        if (!existingUserRefreshToken.isEmpty()) {
            BeanUtils.copyProperties(userRefreshToken, existingUserRefreshToken.get(), "username");
            return userRefreshTokenRepository.save(existingUserRefreshToken.get());
        }
        return userRefreshTokenRepository.save(userRefreshToken);
    }

    public String getRefreshTokenByUsername(@NotNull String username) {
        Optional<UserRefreshToken> userRefreshToken = userRefreshTokenRepository.findByUsername(username);
        if (userRefreshToken.isEmpty()) {
            log.info("User has no refresh token");
            return null;
        }
        return userRefreshToken.get().getRefresh_token().toString();
    }

    public boolean checkValidAccessToken(String tokenId) {
        log.info("access_token_id: " + UUID.fromString(tokenId));
        return userRefreshTokenRepository.existsByAti(UUID.fromString(tokenId));
    }

    @Override
    public void deleteUserToken(String username) {
        userRefreshTokenRepository.deleteById(username);
    }
}
