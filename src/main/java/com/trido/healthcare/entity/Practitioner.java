package com.trido.healthcare.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.trido.healthcare.config.jackson.GenderDeserializer;
import com.trido.healthcare.config.jackson.GenderToStringSerializer;
import com.trido.healthcare.entity.enumm.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Practitioner extends BaseEntity {
    @NotNull
    @JsonSerialize(using = GenderToStringSerializer.class)
    @JsonDeserialize(using = GenderDeserializer.class)
    @Enumerated(EnumType.STRING)
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
    @Column(name = "avatar_url")
    private String avatarFileName;
}
