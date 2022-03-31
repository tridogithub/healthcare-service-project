package com.trido.healthcare.service.impl;

import com.trido.healthcare.constants.ConstantMessages;
import com.trido.healthcare.controller.dto.UserDto;
import com.trido.healthcare.controller.mapper.UserMapper;
import com.trido.healthcare.entity.User;
import com.trido.healthcare.exception.InvalidRequestException;
import com.trido.healthcare.repository.UserRepository;
import com.trido.healthcare.service.PatientService;
import com.trido.healthcare.service.PractitionerService;
import com.trido.healthcare.service.UserRefreshTokenService;
import com.trido.healthcare.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRefreshTokenService userRefreshTokenService;
    private final PractitionerService practitionerService;
    private final PatientService patientService;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           UserRefreshTokenService userRefreshTokenService,
                           @Lazy PractitionerService practitionerService,
                           @Lazy PatientService patientService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRefreshTokenService = userRefreshTokenService;
        this.practitionerService = practitionerService;
        this.patientService = patientService;
    }

    public UserDto getUserDtoByUserId(UUID userId) {
        Optional<User> user = userRepository.findByIdAndIsDeleted(userId, false);
        if (user.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.USER_ID_NOT_FOUND, userId), HttpStatus.NOT_FOUND);
        }
        return userMapper.toDto(user.get());
    }

    public List<UserDto> getAllUser() {
        List<User> users = userRepository.findAllByIsDeleted(false);
        return users.stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    public UserDto createNewUser(UserDto userDto) {
        //check existing username
        if (userDto.getUsername() == null
                || userDto.getUsername().equals("")
                || checkExistingUsername(userDto.getUsername())
        ) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.INVALID_USERNAME, userDto.getUsername()), HttpStatus.BAD_REQUEST);
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        userDto.setUserId(savedUser.getId());
        return userDto;
    }

    public boolean deleteUser(UUID userId) {
        Optional<User> user = userRepository.findByIdAndIsDeleted(userId, false);
        if (user.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.INVALID_USER_ID, userId), HttpStatus.NOT_FOUND);
        }
        user.get().setIsDeleted(true);
        userRepository.save(user.get());
        //disable relevant patient or practitioner
        disablePatientPractitioner(userId);
        //delete all token
        userRefreshTokenService.deleteUserToken(user.get().getUsername());
        return true;
    }

    @Override
    public Optional<User> getUserById(UUID userId) {
        return userRepository.findByIdAndIsDeleted(userId, false);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeleted(username, false);
    }

    private boolean checkExistingUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private void disablePatientPractitioner(UUID userId) {
        try {
            patientService.disablePatient(userId);
            practitionerService.disablePractitioner(userId);
        } catch (InvalidRequestException e) {
            log.info("Disable patient/practitioner when delete user.");
        }
    }
}
