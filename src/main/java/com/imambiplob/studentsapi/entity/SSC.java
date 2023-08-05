package com.imambiplob.studentsapi.entity;

import com.imambiplob.studentsapi.enums.Grade;
import com.imambiplob.studentsapi.enums.SSCSubject;
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
@Table(name = "ssc_tbl")
public class SSC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sscId;

    @ElementCollection
    @CollectionTable(name = "ssc_subjects", joinColumns = @JoinColumn(name = "sscId", referencedColumnName = "sscId"))
    private List<String> sscSubjects = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "ssc_grades", joinColumns = @JoinColumn(name = "sscId", referencedColumnName = "sscId"))
    private List<String> sscGrades = new ArrayList<>();

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    @Enumerated(EnumType.STRING)
    private Map<SSCSubject, Grade> subjectGradeMap = new HashMap<>();

}