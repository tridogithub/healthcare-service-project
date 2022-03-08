package com.trido.healthcare.controller.mapper;

import com.trido.healthcare.constants.ConstantMessages;
import com.trido.healthcare.controller.dto.UserDto;
import com.trido.healthcare.entity.Role;
import com.trido.healthcare.entity.User;
import com.trido.healthcare.exception.InvalidRequestException;
import com.trido.healthcare.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class UserMapper {
    private final RoleRepository roleRepository;

    @Autowired
    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public User toEntity(UserDto userDto) {
        User user = new User();
        if (userDto != null) {
            Optional<Role> role = roleRepository.findByRoleName(userDto.getRoleName());
            if (role.isEmpty()) {
                throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                        String.format(ConstantMessages.INVALID_ROLE_NAME, userDto.getRoleName()), HttpStatus.BAD_REQUEST);
            }
            user.setAuthority(userDto.getAuthority() == null ? userDto.getRoleName() : null);
            user.setUsername(userDto.getUsername().toLowerCase());
            user.setPassword(userDto.getPassword());
            user.setRoleId(role.get().getId());
        }
        return user;
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getId());
        userDto.setAuthority(user.getAuthority());
        Optional<Role> role = roleRepository.findById(user.getRoleId());
        userDto.setRoleName(role.get().getRoleName());
        userDto.setUsername(user.getUsername());
        return userDto;
    }
}
