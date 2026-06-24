package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.LoginRequestDto;
import com.harshit.smartspend.dto.LoginResponseDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto dto);
}
