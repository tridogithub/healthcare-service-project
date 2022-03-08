package com.trido.healthcare.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    Male('M', "Male"),
    Female('F', "Female"),
    Other('O', "Other");
    private final Character code;
    private final String description;
}
