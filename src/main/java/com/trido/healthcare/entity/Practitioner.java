package com.trido.healthcare.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Practitioner extends BaseEntity {
    @NotNull
    private Gender gender;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private Boolean active = true;
    @NotNull
    private LocalDate birthDate;
    @NotNull
    private Integer experience;
    @NotNull
    private String email;
    private String avatarUrl;
}
