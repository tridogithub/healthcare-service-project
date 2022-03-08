package com.trido.healthcare.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Patient extends BaseEntity {
    @NotNull
    private Gender gender;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String email;
    @NotNull
    private Boolean active = true;
    private LocalDate birthDate;
    private String avatarUrl;
}
