package com.trido.healthcare.controller.mapper;

import com.trido.healthcare.controller.dto.AppointmentDto;
import com.trido.healthcare.entity.Appointment;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {
    public Appointment toEntity(AppointmentDto appointmentDto) {
        Appointment appointment = new Appointment();
        BeanUtils.copyProperties(appointmentDto, appointment, "id");
        return appointment;
    }

    public AppointmentDto toDto(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        BeanUtils.copyProperties(appointment, appointmentDto);
        return appointmentDto;
    }
}
