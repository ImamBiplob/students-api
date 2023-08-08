package com.imambiplob.studentsapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student_tbl")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "First Name should not be empty")
    @Size(min = 2, max = 50,message = "First Name should be between 2 to 50 characters")
    private String firstName;
    @NotNull(message = "First Name should not be empty")
    @Size(min = 2, max = 50,message = "Last Name should be between 2 to 50 characters")
    private String lastName;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;
    private LocalDate dob;
    @NotNull(message = "Board should not be empty")
    private String board;
    @NotBlank(message = "Password is mandatory")
    private String password;
    private String contact;
    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ssc_id", referencedColumnName = "sscId")
    private SSC ssc;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hsc_id", referencedColumnName = "hscId")
    private HSC hsc;

}