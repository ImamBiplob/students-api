package com.imambiplob.studentsapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {
    @NotBlank(message = "Old Password is mandatory")
    private String oldPassword;
    @NotBlank(message = "New Password is mandatory")
    private String newPassword;
}
