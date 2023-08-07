package com.imambiplob.studentsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectGpaDto {
    private String subject;
    private String gpa;
}
