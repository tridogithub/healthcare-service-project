package com.trido.healthcare.service;

import com.trido.healthcare.controller.dto.UserDto;
import com.trido.healthcare.entity.User;
import com.trido.healthcare.entity.VerificationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    public UserDto getUserDtoByUserId(UUID userId);

    public List<UserDto> getAllUser();

    public UserDto createNewUser(UserDto userDto, HttpServletRequest request);

    public boolean deleteUser(UUID userId);

    Optional<User> getUserById(UUID userId);

    Optional<User> getUserByUsername(String username);

    VerificationToken registrationTokenVerification(String token);

    VerificationToken createVerificationToken(User user);
}
