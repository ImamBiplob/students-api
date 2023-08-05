package com.imambiplob.studentsapi.service;

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

    public void addSubjectGradeMapping(HSC hsc, List<String> subjectLabels, List<String> gradeLabels) {
        Iterator<String> gradesIterator = gradeLabels.iterator();
        for (String subjectLabel : subjectLabels) {
            HSCSubject subject = HSCSubject.getSubjectByLabel(subjectLabel);
            String gradeLabel = gradesIterator.next();
            Grade grade = Grade.getSubjectByLabel(gradeLabel);
            hsc.getSubjectGradeMap().put(subject, grade);
        }
        hscRepository.save(hsc);
    }

    public Grade getGradeForSubject(HSC ssc, HSCSubject subject) {
        return ssc.getSubjectGradeMap().get(subject);
    }
}