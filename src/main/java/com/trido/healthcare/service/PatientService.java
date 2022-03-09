package com.trido.healthcare.service;

import com.trido.healthcare.controller.dto.PatientDto;

import java.util.List;

public interface PatientService {
    List<PatientDto> getAllPatients();

    PatientDto getPatientById(String patientId);

    PatientDto createPatient(PatientDto patientDto);

    PatientDto updatePatient(String patientId, PatientDto patientDto);

    boolean disablePatient(String patientId);
}
