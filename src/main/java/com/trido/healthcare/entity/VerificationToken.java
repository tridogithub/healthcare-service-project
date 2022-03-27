package com.trido.healthcare.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class VerificationToken {
    private static final int EXPIRATION = 60 * 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String token;
    @Column(nullable = false, unique = true, name = "user_id")
    private UUID userId;
    @Column(name = "expiry_date")
    private ZonedDateTime expiryDate;

    public VerificationToken(String token, UUID userId) {
        this.token = token;
        this.userId = userId;
        this.expiryDate = getNewExpiryDate();
    }

    private ZonedDateTime getNewExpiryDate() {
        return ZonedDateTime.now(ZoneId.of("Asia/Bangkok")).plusSeconds(EXPIRATION);
    }
}
