package com.spring_security.Impl.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class LoginResponseDto {
    private LocalTime time;
    private String message;
    private String token;
}
