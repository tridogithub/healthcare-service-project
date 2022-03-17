package com.trido.healthcare.service;

import com.trido.healthcare.controller.dto.AppointmentDto;
import com.trido.healthcare.controller.dto.AppointmentUpdateDto;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    AppointmentDto findAppointmentById(UUID appointmentId);

    List<AppointmentDto> findAllAppointment();

    AppointmentDto addAppointment(AppointmentDto appointmentDto);

    AppointmentDto updateAppointment(UUID appointmentId, AppointmentUpdateDto appointmentUpdateDto);

    boolean disableAppointment(UUID appointmentId);

    List<AppointmentDto> searchAppointments(UUID patientId,
                                            UUID practitionerId,
                                            String status,
                                            ZonedDateTime startTime,
                                            Integer minutesDuration,
                                            Pageable pageable,
                                            List<String> sort
    );

    boolean existsByPatientId(String patientId);

    boolean existsByPractitionerId(String practitionerId);
}
