package com.trido.healthcare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse implements Serializable {
    private String error;
    private String error_description;
}
