package com.trido.healthcare.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class InvalidRequestException extends RuntimeException {
    private LocalDateTime timestamp;
    private String error;
    private String error_description;
    private HttpStatus httpStatus;
}
