package com.trido.healthcare.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.trido.healthcare.config.jackson.GenderDeserializer;
import com.trido.healthcare.config.jackson.GenderToStringSerializer;
import com.trido.healthcare.config.jackson.LocalDateToFormattedStringSerializer;
import com.trido.healthcare.entity.enumm.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PatientDto {
    private UUID id;
    @NotNull
    @JsonSerialize(using = GenderToStringSerializer.class)
    @JsonDeserialize(using = GenderDeserializer.class)
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
    @JsonSerialize(using = LocalDateToFormattedStringSerializer.class)
    private LocalDate birthDate;
}
