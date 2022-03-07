package com.trido.healthcare.config.auth;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BearerContext implements Serializable {
    private String roleName;
    private String userId;
    private String userName;
    private String bearerToken;
}
