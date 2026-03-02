package com.spring_security.Impl.service;


import com.spring_security.Impl.dtos.LoginRequestDto;
import com.spring_security.Impl.dtos.LoginResponseDto;
import com.spring_security.Impl.dtos.SignUpDto;
import com.spring_security.Impl.dtos.SignUpResponse;
import jakarta.validation.Valid;

public interface UserService {
    SignUpResponse register(SignUpDto signUpDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
