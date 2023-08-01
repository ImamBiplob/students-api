package com.imambiplob.studentsapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student_tbl")
public class Student {
    @Id
    @GeneratedValue
    private int id;
    @NotNull(message = "This field should not be empty")
    @Size(min = 3, max = 50,message = "This field should be between 3 to 50 characters")
    private String firstName;
    @NotNull(message = "This field should not be empty")
    @Size(min = 3, max = 50,message = "This field should be between 3 to 50 characters")
    private String lastName;
    @Email(message = "Email should be valid")
    private String email;
    private LocalDate dob;
    private String nationality;
    private String contact;
    private String address;
    private String result1;
    private String result2;
}
