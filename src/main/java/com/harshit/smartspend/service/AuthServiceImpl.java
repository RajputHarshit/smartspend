package com.harshit.smartspend.service;

import com.harshit.smartspend.config.JwtUtil;
import com.harshit.smartspend.dto.LoginRequestDto;
import com.harshit.smartspend.dto.LoginResponseDto;
import com.harshit.smartspend.entity.User;
import com.harshit.smartspend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

//    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.jwtUtil = jwtUtil;
//        this.passwordEncoder = passwordEncoder;
//    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

String token = jwtUtil.generateToken(user.getEmail());

        return LoginResponseDto.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
