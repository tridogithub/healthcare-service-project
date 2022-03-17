package com.trido.healthcare.service;

import com.trido.healthcare.controller.dto.AppointmentDto;
import com.trido.healthcare.entity.Appointment;
import com.trido.healthcare.exception.InvalidRequestException;
import com.trido.healthcare.repository.AppointmentRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.UUID;

@RunWith(SpringRunner.class)
@Import(AppointmentTestConfiguration.class)
public class AppointmentServiceTest {
    @Autowired
    private AppointmentService appointmentService;

    @MockBean
    private AppointmentRepository appointmentRepository;
    @MockBean
    private PatientService patientService;
    @MockBean
    private PractitionerService practitionerService;

    @Before
    public void setup() {

    }

    @Test
    public void whenFindAppointmentWithValidId_thenReturnAppointment() {
        UUID appointmentId = UUID.randomUUID();
        Mockito.when(appointmentRepository.findById(appointmentId)).thenReturn(java.util.Optional.of(new Appointment()));
        Assert.assertNotNull(appointmentService.findAppointmentById(appointmentId));
    }

    @Test
    public void whenFindAppointmentWithInvalidId_thenThrow() {
        UUID appointmentId = UUID.randomUUID();
        Mockito.when(appointmentRepository.findById(appointmentId)).thenReturn(java.util.Optional.of(new Appointment()));
        Assert.assertThrows(InvalidRequestException.class, () -> appointmentService.findAppointmentById(UUID.randomUUID()));
    }

    @Test
    public void findAllAppointment_thenNotNull() {
        Mockito.when(appointmentRepository.findAll()).thenReturn(new ArrayList<>());
        Assert.assertNotNull(appointmentService.findAllAppointment());
    }

    @Test
    public void addAppointment_whenPatientIdNotExist_thenThrowException() {
        Mockito.when(patientService.existsById(Mockito.any())).thenReturn(false);
        Assert.assertThrows(InvalidRequestException.class, () -> {
            appointmentService.addAppointment(new AppointmentDto());
        });
    }

    @Test
    public void addAppointment_whenPractitionerIdNotExist_thenThrowException() {
        Mockito.when(practitionerService.existsById(Mockito.any())).thenReturn(false);
        Assert.assertThrows(InvalidRequestException.class, () -> {
            appointmentService.addAppointment(new AppointmentDto());
        });
    }

    @Test
    public void updateAppointment_whenAppointmentNotExist_thenThrowException() {
        Mockito.when(appointmentRepository.findByIdAndIsDeleted(Mockito.any(), Mockito.anyBoolean())).thenReturn(null);
        Assert.assertThrows(InvalidRequestException.class, () -> {
            appointmentService.updateAppointment(Mockito.any(), Mockito.any());
        });
    }

    @Test
    public void disableAppointment_whenAppointmentNotExist_thenThrowException() {
        Mockito.when(appointmentRepository.findByIdAndIsDeleted(Mockito.any(), Mockito.anyBoolean())).thenReturn(null);
        Assert.assertThrows(InvalidRequestException.class, () -> {
            appointmentService.disableAppointment(UUID.randomUUID());
        });
    }

}
