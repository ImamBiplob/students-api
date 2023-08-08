package com.imambiplob.studentsapi.service;

import com.imambiplob.studentsapi.dto.SubjectGpaDto;
import com.imambiplob.studentsapi.entity.HSC;
import com.imambiplob.studentsapi.enums.Grade;
import com.imambiplob.studentsapi.enums.HSCSubject;
import com.imambiplob.studentsapi.enums.SSCSubject;
import com.imambiplob.studentsapi.repository.HSCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class HSCService {
    private final HSCRepository hscRepository;

    public HSCService(HSCRepository hscRepository) {
        this.hscRepository = hscRepository;
    }

    public void addSubjectGradeMapping(List<SubjectGpaDto> hsc, HSC newHsc) {

        for (SubjectGpaDto subjectGpa : hsc) {
            HSCSubject subject = HSCSubject.getSubjectByLabel(subjectGpa.getSubject());
            Grade grade = Grade.getSubjectByLabel(subjectGpa.getGpa());
            newHsc.getSubjectGradeMap().put(subject, grade);
        }

        hscRepository.save(newHsc);
    }

}