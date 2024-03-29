package com.trido.healthcare.service;

import com.trido.healthcare.controller.dto.PatientDto;
import com.trido.healthcare.domain.PatientFilter;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    List<PatientDto> getAllPatients();

    PatientDto getPatientById(UUID patientId);

    PatientDto createPatient(PatientDto patientDto);

    PatientDto updatePatient(UUID patientId, PatientDto patientDto);

    boolean disablePatient(UUID patientId);

    boolean saveAvatarImage(MultipartFile file, UUID patientId);

    Resource loadAvatarImage(UUID patientId);

    boolean existsById(UUID patientId);

    List<PatientDto> searchPatients(PatientFilter patientFilter, Pageable pageable, List<String> sortValues);
}
