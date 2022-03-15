package com.trido.healthcare.domain;

import com.trido.healthcare.entity.enumm.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PractitionerFilter {
    private Gender gender;
    private String firstName;
    private String lastName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;
    private Integer experience;
    private String email;
}
