package com.spring_security.Impl.controller;


import com.spring_security.Impl.dtos.LoginRequestDto;
import com.spring_security.Impl.dtos.LoginResponseDto;
import com.spring_security.Impl.dtos.SignUpDto;
import com.spring_security.Impl.dtos.SignUpResponse;
import com.spring_security.Impl.entity.User;
import com.spring_security.Impl.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> registerUser(@Valid @RequestBody SignUpDto signUpDto){

        SignUpResponse upResponse = userService.register(signUpDto);

        return ResponseEntity.status(201).body(upResponse);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto){

        LoginResponseDto upResponse = userService.login(loginRequestDto);

        return ResponseEntity.status(200).body(upResponse);

    }
}
