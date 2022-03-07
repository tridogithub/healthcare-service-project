package com.trido.healthcare.repository;

import com.trido.healthcare.entity.token.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, String> {
    Optional<UserRefreshToken> findByUsername(String username);
    boolean existsByAti(UUID ati);

}
