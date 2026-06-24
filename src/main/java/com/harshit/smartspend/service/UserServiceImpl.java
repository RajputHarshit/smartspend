package com.harshit.smartspend.service;

import com.harshit.smartspend.config.PasswordEncoderConfig;
import com.harshit.smartspend.dto.RegisterRequestDto;
import com.harshit.smartspend.dto.RegisterResponseDto;
import com.harshit.smartspend.entity.User;
import com.harshit.smartspend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public RegisterResponseDto registerUser(RegisterRequestDto dto) {
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword())).build();
        User savedUser= userRepository.save(user);

        return RegisterResponseDto
                .builder().id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .createdAt(savedUser.getCreatedAt())
                .build();

    }
}
