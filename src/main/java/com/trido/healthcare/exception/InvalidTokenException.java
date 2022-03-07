package com.trido.healthcare.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvalidTokenException extends RuntimeException {
    private String error;
    private String error_description;
}
