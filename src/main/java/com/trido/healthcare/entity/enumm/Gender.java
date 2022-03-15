package com.trido.healthcare.entity.enumm;

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

    public static Gender getGenderFromCode(String code) {
        if ("M".equals(code)) {
            return Gender.Male;
        } else if ("F".equals(code)) {
            return Gender.Female;
        } else if ("O".equals(code)) {
            return Gender.Other;
        } else {
            return null;
        }
    }

    public static String getEnumName(Gender gender) {
        if (gender.equals(Male)) {
            return "Male";
        } else if (gender.equals(Female)) {
            return "Female";
        } else if (gender.equals(Other)) {
            return "Other";
        } else {
            return null;
        }
    }
}
