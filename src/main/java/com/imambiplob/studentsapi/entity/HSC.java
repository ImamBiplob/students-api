package com.imambiplob.studentsapi.entity;

import com.imambiplob.studentsapi.enums.Grade;
import com.imambiplob.studentsapi.enums.HSCSubject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hsc_tbl")
public class HSC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hscId;

    @ElementCollection
    @CollectionTable(name = "hsc_subjects", joinColumns = @JoinColumn(name = "hscId", referencedColumnName = "hscId"))
    private List<String> hscSubjects = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "hsc_grades", joinColumns = @JoinColumn(name = "hscId", referencedColumnName = "hscId"))
    private List<String> hscGrades = new ArrayList<>();

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    @Enumerated(EnumType.STRING)
    private Map<HSCSubject, Grade> subjectGradeMap = new HashMap<>();

}