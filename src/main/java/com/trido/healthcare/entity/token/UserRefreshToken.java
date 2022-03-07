package com.trido.healthcare.entity.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_refresh_token")
public class UserRefreshToken {
    @Id
    private String username;
    private UUID refresh_token_id;
    @Basic(fetch = FetchType.LAZY)
    private byte[] refresh_token;
    @Column(name = "access_token_id")
    private UUID ati;
    private Integer refresh_token_expiration;
}
