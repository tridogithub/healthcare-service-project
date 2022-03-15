package com.trido.healthcare.domain;

import com.trido.healthcare.entity.enumm.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PatientFilter {
    private Gender gender;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;
}
