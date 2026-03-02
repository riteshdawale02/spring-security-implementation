package com.spring_security.Impl.serviceImpl;

import com.spring_security.Impl.dtos.LoginRequestDto;
import com.spring_security.Impl.dtos.LoginResponseDto;
import com.spring_security.Impl.dtos.SignUpDto;
import com.spring_security.Impl.dtos.SignUpResponse;
import com.spring_security.Impl.entity.Role;
import com.spring_security.Impl.entity.User;
import com.spring_security.Impl.repository.UserRepository;
import com.spring_security.Impl.secuity.CustomUserDetails;
import com.spring_security.Impl.secuity.CustomUserDetailsService;
import com.spring_security.Impl.secuity.JwtService;
import com.spring_security.Impl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.parsing.PassThroughSourceExtractor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService){
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SignUpResponse register(SignUpDto signUpDto) {
        if (userRepository.existsByEmail(signUpDto.getEmail())){
            throw new RuntimeException("User exist with same email");
        }


        User user = User.builder()
                .email(signUpDto.getEmail())
                .name(signUpDto.getName())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        return  SignUpResponse.builder()
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .message("User Registered Successfully!")
                .build();

    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));


        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        String token = jwtService.generateToken(user.getId(), user.getEmail());

        log.info("Login successfully: "+ user.getEmail());

        return LoginResponseDto.builder()
                .token(token)
                .message("Login Successfully...")
                .time(LocalTime.now())
                .build();
    }
}
