package com.imambiplob.studentsapi.dto;

import com.imambiplob.studentsapi.entity.Student;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
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

    @Size(min = 2, max = 50,message = "First Name should be between 2 to 50 characters")
    private String firstName;

    @Size(min = 2, max = 50,message = "Last Name should be between 2 to 50 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;
    private String contact;
    private LocalDate dob;
    private String board;
    private String address;

    private List<SubjectGpaDto> ssc;
    private List<SubjectGpaDto> hsc;

    public StudentDashboard(Student student) {
        id = student.getId();
    }
}
