package com.trido.healthcare.controller;

import com.trido.healthcare.controller.dto.UserDto;
import com.trido.healthcare.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("${api.prefix}")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") UUID userId) {
        UserDto userDto = userServiceImpl.getUserDtoByUserId(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<UserDto> results = userServiceImpl.getAllUser();
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping("/users/registry")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        UserDto savedUserDto = userServiceImpl.createNewUser(userDto);
        return new ResponseEntity<>(savedUserDto, HttpStatus.OK);
    }

    @DeleteMapping("users/{userId}")
    public ResponseEntity<?> disableUserById(@PathVariable("userId") UUID userId) {
        boolean result = userServiceImpl.deleteUser(userId);
        return new ResponseEntity<>(result ? "User has been deleted" : "Fail to delete user", HttpStatus.OK);
    }


}
