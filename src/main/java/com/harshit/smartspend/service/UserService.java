package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.RegisterRequestDto;
import com.harshit.smartspend.dto.RegisterResponseDto;
import com.harshit.smartspend.entity.User;
import org.springframework.stereotype.Service;


public interface UserService {
   RegisterResponseDto registerUser(RegisterRequestDto dto);

   Long getUserIdByEmail(String email);
}
