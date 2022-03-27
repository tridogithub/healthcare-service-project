package com.trido.healthcare.service.impl;

import com.trido.healthcare.constants.ConstantMessages;
import com.trido.healthcare.controller.dto.UserDto;
import com.trido.healthcare.controller.mapper.UserMapper;
import com.trido.healthcare.entity.User;
import com.trido.healthcare.entity.VerificationToken;
import com.trido.healthcare.exception.InvalidRequestException;
import com.trido.healthcare.registration.OnRegistrationCompleteEvent;
import com.trido.healthcare.repository.UserRepository;
import com.trido.healthcare.repository.VerificationTokenRepository;
import com.trido.healthcare.service.PatientService;
import com.trido.healthcare.service.PractitionerService;
import com.trido.healthcare.service.UserRefreshTokenService;
import com.trido.healthcare.service.UserService;
import liquibase.pro.packaged.U;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRefreshTokenService userRefreshTokenService;
    @Autowired
    private PractitionerService practitionerService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

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
        List<UserDto> results = new ArrayList<>();
        users.forEach(user -> results.add(userMapper.toDto(user)));
        return results;
    }

    public UserDto createNewUser(UserDto userDto, HttpServletRequest request) {
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
        user.setEnabled(false);
        User savedUser = userRepository.save(user);
        userDto.setUserId(savedUser.getId());
        //validate email
        String appUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(savedUser, appUrl));
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

    @Override
    public VerificationToken registrationTokenVerification(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);

        if (!verificationToken.isEmpty() && !isVerificationTokenExpired(verificationToken.get())) {
            Optional<User> user = userRepository.findByIdAndIsDeleted(verificationToken.get().getUserId(), false);
            user.get().setEnabled(true);
            userRepository.save(user.get());
            verificationTokenRepository.delete(verificationToken.get());
            return verificationToken.get();
        }
        return null;
    }

    @Override
    public VerificationToken createVerificationToken(@NotNull User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken newVerificationToken = new VerificationToken(token, user.getId());
        VerificationToken savedVerificationToken = verificationTokenRepository.save(newVerificationToken);
        return savedVerificationToken;
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

    private boolean isVerificationTokenExpired(VerificationToken verificationToken) {
        ZonedDateTime currentZoneDateTime = ZonedDateTime.now(ZoneId.of("Asia/Bangkok"));
        return currentZoneDateTime.isEqual(verificationToken.getExpiryDate());
    }
}
