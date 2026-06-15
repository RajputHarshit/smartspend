package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.RegisterRequestDto;
import com.harshit.smartspend.entity.User;
import com.harshit.smartspend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(RegisterRequestDto dto) {
        User user = User.builder().name(dto.getName())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword()).build();
        return userRepository.save(user);

    }
}
