package com.trido.healthcare.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.trido.healthcare.config.jackson.DateToSecondSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse implements Serializable {
    private String user_id;
    private String role;
    private String token_type;
    private String access_token;
    private String refresh_token;
    @JsonSerialize(using = DateToSecondSerializer.class)
    private Date exp;
}
