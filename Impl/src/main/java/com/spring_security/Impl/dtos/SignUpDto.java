package com.spring_security.Impl.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpDto {

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 character")
    private String password;

    @NotBlank(message = "Name is Required")
    @Size(min = 2, max = 20, message = "Name must be between 2 to 20 character")
    private String name;


}
