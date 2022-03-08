package com.trido.healthcare.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.trido.healthcare.config.jackson.LocalDateTimeToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse implements Serializable {
    @JsonSerialize(using = LocalDateTimeToStringSerializer.class)
    private LocalDateTime timestamp;
    private String error;
    private String error_description;
    private HttpStatus httpStatus;
}
