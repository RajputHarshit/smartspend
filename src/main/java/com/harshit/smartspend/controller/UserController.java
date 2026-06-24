package com.harshit.smartspend.controller;

import com.harshit.smartspend.dto.RegisterRequestDto;
import com.harshit.smartspend.dto.RegisterResponseDto;
import com.harshit.smartspend.entity.User;
import com.harshit.smartspend.service.UserService;
import com.harshit.smartspend.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
 private UserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto dto){
        RegisterResponseDto registerResponseDto =userService.registerUser(dto);
        return new ResponseEntity<>(registerResponseDto, HttpStatus.CREATED);
    }
}
