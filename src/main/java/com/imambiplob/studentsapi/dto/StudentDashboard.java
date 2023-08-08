package com.imambiplob.studentsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDashboard {
    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private String contact;
    private LocalDate dob;

    private String board;
    private String address;

    private List<SubjectGpaDto> ssc;
    private List<SubjectGpaDto> hsc;
}
