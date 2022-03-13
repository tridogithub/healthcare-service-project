package com.trido.healthcare.controller.mapper;

import com.trido.healthcare.controller.dto.PatientDto;
import com.trido.healthcare.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public Patient toEntity(PatientDto patientDto) {
        Patient patient = new Patient();
        patient.setGender(patientDto.getGender());
        patient.setFirstName(patientDto.getFirstName());
        patient.setLastName(patientDto.getLastName());
        patient.setPhoneNumber(patientDto.getPhoneNumber());
        patient.setEmail(patientDto.getEmail());
        patient.setBirthDate(patientDto.getBirthDate());
        return patient;
    }

    public PatientDto toDto(Patient patient) {
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patient.getId());
        patientDto.setGender(patient.getGender());
        patientDto.setFirstName(patient.getFirstName());
        patientDto.setLastName(patient.getLastName());
        patientDto.setPhoneNumber(patient.getPhoneNumber());
        patientDto.setEmail(patient.getEmail());
        patientDto.setBirthDate(patient.getBirthDate());
        return patientDto;
    }
}
