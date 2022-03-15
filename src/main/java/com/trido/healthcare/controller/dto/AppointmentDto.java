package com.trido.healthcare.controller.dto;

import com.trido.healthcare.entity.enumm.AppointmentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AppointmentDto {
    private UUID id;
    private UUID patientId;
    private UUID practitionerId;
    private AppointmentStatus status;
    private ZonedDateTime startTime;
    private Integer minutesDuration;
    private String patientInstruction;
}
