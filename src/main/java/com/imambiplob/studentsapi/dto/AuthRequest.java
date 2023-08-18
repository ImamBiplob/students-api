package com.imambiplob.studentsapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @NotBlank(message = "Email is Mandatory")
    @Email(message = "Email should be Valid")
    private String email;
    @NotBlank(message = "Password is mandatory")
    private String password;

}
