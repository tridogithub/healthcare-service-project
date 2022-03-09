package com.trido.healthcare.service.impl;

import com.trido.healthcare.config.auth.BearerContextHolder;
import com.trido.healthcare.constants.ConstantMessages;
import com.trido.healthcare.controller.dto.PractitionerDto;
import com.trido.healthcare.controller.mapper.PractitionerMapper;
import com.trido.healthcare.entity.Practitioner;
import com.trido.healthcare.exception.InvalidRequestException;
import com.trido.healthcare.repository.PractitionerRepository;
import com.trido.healthcare.service.PractitionerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PractitionerServiceImpl implements PractitionerService {
    @Autowired
    private PractitionerRepository practitionerRepository;

    @Autowired
    private PractitionerMapper practitionerMapper;

    @Override
    public List<PractitionerDto> getAllPractitioners() {
        List<Practitioner> practitionerList = practitionerRepository.findAll();
        List<PractitionerDto> practitionerDtoList = new ArrayList<>();
        practitionerList.forEach(practitioner -> practitionerDtoList.add(practitionerMapper.toDto(practitioner)));
        return practitionerDtoList;
    }

    @Override
    public PractitionerDto getPractitionerById(String practitionerId) {
        Optional<Practitioner> practitioner = practitionerRepository.findById(UUID.fromString(practitionerId));
        if (practitioner.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.USER_ID_NOT_FOUND, practitionerId), HttpStatus.NOT_FOUND);
        }
        return practitionerMapper.toDto(practitioner.get());
    }

    @Override
    public PractitionerDto createPractitioner(PractitionerDto practitionerDto) {
        if (practitionerDto.getEmail() == null || practitionerDto.getEmail().equals("")
        ) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    ConstantMessages.MANDATORY_EMAIL, HttpStatus.BAD_REQUEST);
        }
        Practitioner practitioner = practitionerMapper.toEntity(practitionerDto);
        practitioner.setId(UUID.fromString(BearerContextHolder.getContext().getUserId()));
        Practitioner savedPractitioner = practitionerRepository.save(practitioner);
        return practitionerMapper.toDto(savedPractitioner);
    }

    @Override
    public PractitionerDto updatePractitioner(String practitionerId, PractitionerDto practitionerDto) {
        Optional<Practitioner> savedPractitioner = practitionerRepository.findById(UUID.fromString(practitionerId));
        if (savedPractitioner.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.PRACTITIONER_ID_NOT_FOUND, practitionerId), HttpStatus.BAD_REQUEST);
        }
        BeanUtils.copyProperties(practitionerDto, savedPractitioner, "id");
        Practitioner updatedPractitioner = practitionerRepository.save(savedPractitioner.get());
        return practitionerMapper.toDto(updatedPractitioner);
    }

    @Override
    public boolean disablePractitioner(String practitionerId) {
        Optional<Practitioner> savedPractitioner = practitionerRepository.findById(UUID.fromString(practitionerId));
        if (savedPractitioner.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.PRACTITIONER_ID_NOT_FOUND, practitionerId), HttpStatus.BAD_REQUEST);
        }
        savedPractitioner.get().setActive(false);
        practitionerRepository.save(savedPractitioner.get());
        return true;
    }
}
