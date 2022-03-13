package com.trido.healthcare.service.impl;

import com.trido.healthcare.config.auth.BearerContextHolder;
import com.trido.healthcare.constants.ConstantMessages;
import com.trido.healthcare.controller.dto.PractitionerDto;
import com.trido.healthcare.controller.mapper.PractitionerMapper;
import com.trido.healthcare.entity.Practitioner;
import com.trido.healthcare.entity.User;
import com.trido.healthcare.exception.InvalidRequestException;
import com.trido.healthcare.exception.StorageNotFoundException;
import com.trido.healthcare.repository.PractitionerRepository;
import com.trido.healthcare.service.PractitionerService;
import com.trido.healthcare.service.StorageService;
import com.trido.healthcare.service.UserService;
import org.apache.http.entity.ContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
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
public class PractitionerServiceImpl implements PractitionerService {
    private static final List<String> imageTypes = Arrays.asList(
            ContentType.IMAGE_JPEG.getMimeType(),
            ContentType.IMAGE_GIF.getMimeType(),
            ContentType.IMAGE_PNG.getMimeType()
    );
    @Autowired
    private PractitionerRepository practitionerRepository;

    @Autowired
    private PractitionerMapper practitionerMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;

    @Override
    public List<PractitionerDto> getAllPractitioners() {
        List<Practitioner> practitionerList = practitionerRepository.findAllByActive(true);
        List<PractitionerDto> practitionerDtoList = new ArrayList<>();
        practitionerList.forEach(practitioner -> practitionerDtoList.add(practitionerMapper.toDto(practitioner)));
        return practitionerDtoList;
    }

    @Override
    public PractitionerDto getPractitionerById(UUID practitionerId) {
        Optional<Practitioner> practitioner = practitionerRepository.findByIdAndActive(practitionerId, true);
        if (practitioner.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.USER_ID_NOT_FOUND, practitionerId), HttpStatus.NOT_FOUND);
        }
        return practitionerMapper.toDto(practitioner.get());
    }

    @Override
    public PractitionerDto createPractitioner(PractitionerDto practitionerDto) {
        Optional<User> currentUser = userService.getUserByUsername(BearerContextHolder.getContext().getUserName());
        if (currentUser.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    ConstantMessages.CURRENT_USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        if (practitionerRepository.existsById(currentUser.get().getId())) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    ConstantMessages.PRACTITIONER_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
        }
        if (practitionerDto.getEmail() == null || practitionerDto.getEmail().equals("")
        ) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    ConstantMessages.MANDATORY_EMAIL, HttpStatus.BAD_REQUEST);
        }
        Practitioner practitioner = practitionerMapper.toEntity(practitionerDto);
        practitioner.setId(currentUser.get().getId());
        Practitioner savedPractitioner = practitionerRepository.save(practitioner);
        return practitionerMapper.toDto(savedPractitioner);
    }

    @Override
    public PractitionerDto updatePractitioner(UUID practitionerId, PractitionerDto practitionerDto) {
        Optional<Practitioner> savedPractitioner = practitionerRepository.findByIdAndActive(practitionerId, true);
        if (savedPractitioner.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.PRACTITIONER_ID_NOT_FOUND, practitionerId), HttpStatus.BAD_REQUEST);
        }
        BeanUtils.copyProperties(practitionerDto, savedPractitioner, "id");
        Practitioner updatedPractitioner = practitionerRepository.save(savedPractitioner.get());
        return practitionerMapper.toDto(updatedPractitioner);
    }

    @Override
    public boolean disablePractitioner(UUID practitionerId) {
        Optional<Practitioner> savedPractitioner = practitionerRepository.findByIdAndActive(practitionerId, true);
        if (savedPractitioner.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.PRACTITIONER_ID_NOT_FOUND, practitionerId), HttpStatus.BAD_REQUEST);
        }
        savedPractitioner.get().setActive(false);
        practitionerRepository.save(savedPractitioner.get());
        return true;
    }

    @Override
    public boolean saveAvatarImage(MultipartFile file, UUID practitionerId) {
        if (!imageTypes.contains(file.getContentType())) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    ConstantMessages.INVALID_UPLOADED_FILE_TYPE, HttpStatus.BAD_REQUEST);
        }
        Optional<Practitioner> practitioner = practitionerRepository.findByIdAndActive(practitionerId, true);
        if (practitioner.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.PATIENT_ID_NOT_FOUND, practitionerId), HttpStatus.BAD_REQUEST);
        }
        Path filePath = storageService.saveImage(file, practitionerId);
        practitioner.get().setAvatarFileName(filePath.getFileName().toString());
        practitionerRepository.save(practitioner.get());
        return true;
    }

    @Override
    public Resource loadAvatarImage(UUID practitionerId) {
        Optional<Practitioner> practitioner = practitionerRepository.findByIdAndActive(practitionerId, true);
        if (practitioner.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.PATIENT_ID_NOT_FOUND, practitionerId), HttpStatus.BAD_REQUEST);
        }
        Resource file = null;
        try {
            file = storageService.loadImage(practitioner.get().getAvatarFileName(), practitionerId);
        } catch (IOException e) {
            throw new StorageNotFoundException(ConstantMessages.NOT_FOUND_AVATAR);
        }
        return file;
    }
}
