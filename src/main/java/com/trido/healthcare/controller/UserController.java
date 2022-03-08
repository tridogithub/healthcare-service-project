package com.trido.healthcare.controller;

import com.trido.healthcare.controller.dto.UserDto;
import com.trido.healthcare.service.UserService;
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

@Controller
@RequestMapping("${api.prefix}")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity getUserById(@PathVariable("userId") String userId) {
        UserDto userDto = userService.getUserDtoByUserId(userId);
        return new ResponseEntity(userDto, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity getAllUsers() {
        List<UserDto> results = userService.getAllUser();
        return new ResponseEntity(results, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody UserDto userDto) {
        UserDto savedUserDto = userService.createNewUser(userDto);
        return new ResponseEntity(savedUserDto, HttpStatus.OK);
    }

    @DeleteMapping("users/{userId}")
    public ResponseEntity disableUserById(@PathVariable("userId") String userId) {
        boolean result = userService.disableUser(userId);
        return new ResponseEntity("User has been deleted", HttpStatus.OK);
    }


}
