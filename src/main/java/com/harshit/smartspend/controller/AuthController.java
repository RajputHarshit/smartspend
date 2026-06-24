package com.harshit.smartspend.controller;

import com.harshit.smartspend.dto.LoginRequestDto;
import com.harshit.smartspend.dto.LoginResponseDto;
import com.harshit.smartspend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        LoginResponseDto response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}
