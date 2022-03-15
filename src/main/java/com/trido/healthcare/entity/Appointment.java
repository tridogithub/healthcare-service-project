package com.trido.healthcare.entity;

import com.trido.healthcare.entity.enumm.AppointmentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Appointment extends BaseEntity {
    private UUID patientId;
    private UUID practitionerId;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    private ZonedDateTime startTime;
    private Integer minutesDuration;
    private String patientInstruction;
}
