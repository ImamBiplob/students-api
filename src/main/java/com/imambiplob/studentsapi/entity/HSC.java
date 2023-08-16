package com.imambiplob.studentsapi.entity;

import com.imambiplob.studentsapi.enums.Grade;
import com.imambiplob.studentsapi.enums.HSCSubject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
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
    @MapKeyEnumerated(EnumType.STRING)
    @Enumerated(EnumType.STRING)
    private Map<HSCSubject, Grade> subjectGradeMap = new HashMap<>();

}