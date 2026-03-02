package com.spring_security.Impl.dtos;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpResponse {
    private String message;
    private  String email;
    private String name;
}
