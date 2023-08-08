package com.imambiplob.studentsapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterStudent {
    @NotNull(message = "This field should not be empty")
    @Size(min = 2, max = 50,message = "First Name should be between 2 to 50 characters")
    private String firstName;
    @NotNull(message = "This field should not be empty")
    @Size(min = 2, max = 50,message = "Last Name should be between 2 to 50 characters")
    private String lastName;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Password is mandatory")
    private String password;
    private String contact;
    private LocalDate dob;
    @NotNull(message = "This field should not be empty")
    private String board;
    private String address;

    private List<SubjectGpaDto> ssc;
    private List<SubjectGpaDto> hsc;
}
