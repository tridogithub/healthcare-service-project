package com.trido.healthcare.service.impl;

import com.trido.healthcare.config.auth.BearerContextHolder;
import com.trido.healthcare.constants.ConstantMessages;
import com.trido.healthcare.controller.dto.PatientDto;
import com.trido.healthcare.controller.mapper.PatientMapper;
import com.trido.healthcare.entity.Patient;
import com.trido.healthcare.exception.InvalidRequestException;
import com.trido.healthcare.repository.PatientRepository;
import com.trido.healthcare.service.PatientService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientMapper patientMapper;

    @Override
    public List<PatientDto> getAllPatients() {
        List<Patient> patients = patientRepository.findAllByIsDeleted(false);
        List<PatientDto> patientDtoList = new ArrayList<>();
        patients.forEach(patient -> patientDtoList.add(patientMapper.toDto(patient)));
        return patientDtoList;
    }

    @Override
    public PatientDto getPatientById(String patientId) {
        Patient patient = patientRepository.findByIdAndIsDeleted(UUID.fromString(patientId), false);
        return patientMapper.toDto(patient);
    }

    @Override
    public PatientDto createPatient(@NotNull PatientDto patientDto) {
        if (patientDto.getEmail() == null
                || patientDto.getEmail().equals("")) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    ConstantMessages.MANDATORY_EMAIL, HttpStatus.BAD_REQUEST);
        }
        Patient patient = patientMapper.toEntity(patientDto);
        patient.setId(UUID.fromString(BearerContextHolder.getContext().getUserId()));
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toDto(savedPatient);
    }

    @Override
    public PatientDto updatePatient(String patientId, PatientDto patientDto) {
        Patient patient = patientRepository.findByIdAndIsDeleted(UUID.fromString(patientId), false);
        if (patient == null) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.PATIENT_ID_NOT_FOUND, patientId), HttpStatus.BAD_REQUEST);
        }
        BeanUtils.copyProperties(patientDto, patient, "id");
        Patient updatedPatient = patientRepository.save(patient);
        return patientMapper.toDto(patient);
    }

    @Override
    public boolean disablePatient(String patientId) {
        Patient patient = patientRepository.findByIdAndIsDeleted(UUID.fromString(patientId), false);
        if (patient == null) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.PATIENT_ID_NOT_FOUND, patientId), HttpStatus.BAD_REQUEST);
        }
        patient.setActive(false);
        patientRepository.save(patient);
        return true;
    }
}
