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
    @MapKeyEnumerated(EnumType.STRING)
    @Enumerated(EnumType.STRING)
    private Map<SSCSubject, Grade> subjectGradeMap = new HashMap<>();

}