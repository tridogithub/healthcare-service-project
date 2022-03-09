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
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PractitionerService practitionerService;
    private final PatientService patientService;
    private final UserRefreshTokenService userRefreshTokenService;

    public UserDto getUserDtoByUserId(String userId) {
        Optional<User> user = userRepository.findByIdAndIsDeleted(UUID.fromString(userId), false);
        if (user.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    String.format(ConstantMessages.USER_ID_NOT_FOUND, userId), HttpStatus.NOT_FOUND);
        }
        return userMapper.toDto(user.get());
    }

    public List<UserDto> getAllUser() {
        List<User> users = userRepository.findAllByIsDeleted(false);
        List<UserDto> results = new ArrayList<>();
        users.forEach(user -> results.add(userMapper.toDto(user)));
        return results;
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

    public boolean deleteUser(String userId) {
        Optional<User> user = userRepository.findByIdAndIsDeleted(UUID.fromString(userId), false);
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

    private boolean checkExistingUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private void disablePatientPractitioner(String userId) {
        try {
            patientService.disablePatient(userId);
            practitionerService.disablePractitioner(userId);
        } catch (InvalidRequestException e) {

        }
    }
}
