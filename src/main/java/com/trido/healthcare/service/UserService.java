package com.trido.healthcare.service;

import com.trido.healthcare.controller.dto.UserDto;

import java.util.List;

public interface UserService {
    public UserDto getUserDtoByUserId(String userId);

    public List<UserDto> getAllUser();

    public UserDto createNewUser(UserDto userDto);

    public boolean deleteUser(String userId);
}
