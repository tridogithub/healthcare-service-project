package com.trido.healthcare.controller.dto;

import com.trido.healthcare.entity.enumm.AppointmentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class AppointmentUpdateDto {
    private AppointmentStatus status;
    private ZonedDateTime startTime;
    private Integer minutesDuration;
    private String patientInstruction;
}
