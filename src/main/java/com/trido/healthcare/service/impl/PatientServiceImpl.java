package com.trido.healthcare.service.impl;

import com.trido.healthcare.config.auth.BearerContextHolder;
import com.trido.healthcare.constants.ConstantMessages;
import com.trido.healthcare.controller.dto.PatientDto;
import com.trido.healthcare.controller.mapper.PatientMapper;
import com.trido.healthcare.domain.PatientFilter;
import com.trido.healthcare.entity.Patient;
import com.trido.healthcare.entity.User;
import com.trido.healthcare.entity.enumm.Gender;
import com.trido.healthcare.exception.InvalidRequestException;
import com.trido.healthcare.exception.StorageNotFoundException;
import com.trido.healthcare.repository.PatientRepository;
import com.trido.healthcare.service.PatientService;
import com.trido.healthcare.service.StorageService;
import com.trido.healthcare.service.UserService;
import com.trido.healthcare.util.SearchUtils;
import com.trido.healthcare.util.search.PatientSearchSpecification;
import org.apache.http.entity.ContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {
    private static final List<String> imageTypes = Arrays.asList(
            ContentType.IMAGE_JPEG.getMimeType(),
            ContentType.IMAGE_GIF.getMimeType(),
            ContentType.IMAGE_PNG.getMimeType()
    );

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;

    @Override
    public List<PatientDto> getAllPatients() {
        List<Patient> patients = patientRepository.findAllByActive(true);
        List<PatientDto> patientDtoList = new ArrayList<>();
        patients.forEach(patient -> patientDtoList.add(patientMapper.toDto(patient)));
        return patientDtoList;
    }

    @Override
    public PatientDto getPatientById(UUID patientId) {
        Patient patient = patientRepository.findByIdAndActive(patientId, true);
        return patientMapper.toDto(patient);
    }

    @Override
    public PatientDto createPatient(@NotNull PatientDto patientDto) {
        String username = BearerContextHolder.getContext().getUserName();
        Optional<User> currentUser = userService.getUserByUsername(username);
        if (currentUser.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    ConstantMessages.CURRENT_USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        if (patientRepository.existsById(currentUser.get().getId())) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    ConstantMessages.PATIENT_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
        }
        if (patientDto.getEmail() == null || patientDto.getEmail().equals("")) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    ConstantMessages.MANDATORY_EMAIL, HttpStatus.BAD_REQUEST);
        }
        Patient patient = patientMapper.toEntity(patientDto);
        patient.setId(currentUser.get().getId());
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toDto(savedPatient);
    }

    @Override
    public PatientDto updatePatient(UUID patientId, PatientDto patientDto) {
        Patient patient = patientRepository.findByIdAndActive(patientId, true);
        if (patient == null) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.PATIENT_ID_NOT_FOUND, patientId), HttpStatus.BAD_REQUEST);
        }
        BeanUtils.copyProperties(patientDto, patient, "id");
        Patient updatedPatient = patientRepository.save(patient);
        return patientMapper.toDto(updatedPatient);
    }

    @Override
    public boolean disablePatient(UUID patientId) {
        Patient patient = patientRepository.findByIdAndActive(patientId, true);
        if (patient == null) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.PATIENT_ID_NOT_FOUND, patientId), HttpStatus.BAD_REQUEST);
        }
        patient.setActive(false);
        patientRepository.save(patient);
        return true;
    }

    @Override
    public boolean saveAvatarImage(@NotNull MultipartFile file, UUID patientId) {
        if (!imageTypes.contains(file.getContentType())) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    ConstantMessages.INVALID_UPLOADED_FILE_TYPE, HttpStatus.BAD_REQUEST);
        }
        Patient patient = patientRepository.findByIdAndActive(patientId, true);
        if (patient == null) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.PATIENT_ID_NOT_FOUND, patientId), HttpStatus.BAD_REQUEST);
        }
        Path filePath = storageService.saveImage(file, patientId);
        patient.setAvatarFileName(filePath.getFileName().toString());
        patientRepository.save(patient);
        return true;
    }

    @Override
    public List<PatientDto> searchPatients(PatientFilter patientFilter, Pageable pageable, List<String> sortValues) {
        PatientSearchSpecification patientSearchSpecification =
                new PatientSearchSpecification(Gender.getEnumName(patientFilter.getGender()), patientFilter.getFirstName(),
                        patientFilter.getLastName(), patientFilter.getPhoneNumber(), patientFilter.getEmail(), patientFilter.getBirthDate(), true);
        Pageable patientPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), SearchUtils.getSortFromListParam(sortValues));
        List<Patient> patients = patientRepository.findAll(patientSearchSpecification, patientPageable).getContent();
        List<PatientDto> patientDtoList = new ArrayList<>();
        patients.forEach(patient -> patientDtoList.add(patientMapper.toDto(patient)));
        return patientDtoList;
    }

    @Override
    public Resource loadAvatarImage(UUID patientId) {
        Patient patient = patientRepository.findByIdAndActive(patientId, true);
        if (patient == null) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.PATIENT_ID_NOT_FOUND, patientId), HttpStatus.BAD_REQUEST);
        }
        Resource file = null;
        try {
            file = storageService.loadImage(patient.getAvatarFileName(), patientId);
        } catch (IOException e) {
            throw new StorageNotFoundException(ConstantMessages.NOT_FOUND_AVATAR);
        }
        return file;
    }

    @Override
    public boolean existsById(UUID patientId) {
        return patientRepository.existsByIdAndActive(patientId, true);
    }
}
