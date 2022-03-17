package com.trido.healthcare.service.impl;

import com.trido.healthcare.constants.ConstantMessages;
import com.trido.healthcare.controller.dto.AppointmentDto;
import com.trido.healthcare.controller.dto.AppointmentUpdateDto;
import com.trido.healthcare.controller.mapper.AppointmentMapper;
import com.trido.healthcare.entity.Appointment;
import com.trido.healthcare.exception.InvalidRequestException;
import com.trido.healthcare.repository.AppointmentRepository;
import com.trido.healthcare.service.AppointmentService;
import com.trido.healthcare.service.PatientService;
import com.trido.healthcare.service.PractitionerService;
import com.trido.healthcare.util.SearchUtils;
import com.trido.healthcare.util.search.AppointmentSearchSpecification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private PatientService patientService;
    @Autowired
    private PractitionerService practitionerService;

    @Override
    public AppointmentDto findAppointmentById(UUID appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.APPOINTMENT_ID_NOT_FOUND, appointmentId), HttpStatus.BAD_REQUEST
            );
        }
        AppointmentDto appointmentDto = new AppointmentDto();
        BeanUtils.copyProperties(appointment.get(), appointmentDto);
        return appointmentDto;
    }

    @Override
    public List<AppointmentDto> findAllAppointment() {
        List<Appointment> appointments = appointmentRepository.findAll();
        List<AppointmentDto> appointmentDtoList = new ArrayList<>();
        appointments.forEach(appointment -> appointmentDtoList.add(appointmentMapper.toDto(appointment)));
        return appointmentDtoList;
    }

    @Override
    public AppointmentDto addAppointment(AppointmentDto appointmentDto) {
        if (checkExistingPatientAndPractitioner(appointmentDto.getPatientId(), appointmentDto.getPractitionerId())) {
            appointmentDto.setPatientId(UUID.randomUUID());
            appointmentDto.setPractitionerId(UUID.randomUUID());
            Appointment appointment = appointmentMapper.toEntity(appointmentDto);
            Appointment savedAppointment = appointmentRepository.save(appointment);
            return appointmentMapper.toDto(savedAppointment);
        }
        return null;
    }

    @Override
    public AppointmentDto updateAppointment(UUID appointmentId, AppointmentUpdateDto appointmentUpdateDto) {
        Appointment appointment = appointmentRepository.findByIdAndIsDeleted(appointmentId, false);
        if (appointment == null) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.APPOINTMENT_ID_NOT_FOUND, appointmentId), HttpStatus.BAD_REQUEST
            );
        }
        BeanUtils.copyProperties(appointmentUpdateDto, appointment, "id", "patientId", "practitionerId");
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(updatedAppointment);
    }

    @Override
    public boolean disableAppointment(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findByIdAndIsDeleted(appointmentId, false);
        if (appointment == null) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.APPOINTMENT_ID_NOT_FOUND, appointmentId), HttpStatus.BAD_REQUEST
            );
        }
        appointment.setIsDeleted(true);
        appointmentRepository.save(appointment);
        return true;
    }

    @Override
    public List<AppointmentDto> searchAppointments(UUID patientId,
                                                   UUID practitionerId,
                                                   String status,
                                                   ZonedDateTime startTime,
                                                   Integer minutesDuration,
                                                   Pageable pageable,
                                                   List<String> sort
    ) {
        AppointmentSearchSpecification appointmentSearchSpecification =
                new AppointmentSearchSpecification(patientId, practitionerId, status, startTime, minutesDuration, false);
        Pageable appointmentPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), SearchUtils.getSortFromListParam(sort));

        List<Appointment> appointmentList = appointmentRepository.findAll(appointmentSearchSpecification, appointmentPageable).getContent();
        List<AppointmentDto> appointmentDtoList = new ArrayList<>();
        appointmentList.forEach(appointment -> appointmentDtoList.add(appointmentMapper.toDto(appointment)));
        return appointmentDtoList;
    }

    @Override
    public boolean existsByPatientId(String patientId) {
        return appointmentRepository.existsByPatientId(UUID.fromString(patientId));
    }

    @Override
    public boolean existsByPractitionerId(String practitionerId) {
        return appointmentRepository.existsByPractitionerId(UUID.fromString(practitionerId));
    }

    private boolean checkExistingPatientAndPractitioner(UUID patientId, UUID practitionerId) {
        if (!patientService.existsById(patientId)) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.PATIENT_ID_NOT_FOUND, patientId), HttpStatus.BAD_REQUEST);
        }
        if (practitionerService.existsById(practitionerId)) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.PRACTITIONER_ID_NOT_FOUND, practitionerId), HttpStatus.BAD_REQUEST);
        }
        return true;
    }
}
