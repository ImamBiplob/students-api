package com.imambiplob.studentsapi.service;

import com.imambiplob.studentsapi.dto.SubjectGpaDto;
import com.imambiplob.studentsapi.entity.HSC;
import com.imambiplob.studentsapi.entity.SSC;
import com.imambiplob.studentsapi.enums.Grade;
import com.imambiplob.studentsapi.enums.HSCSubject;
import com.imambiplob.studentsapi.enums.SSCSubject;
import com.imambiplob.studentsapi.repository.SSCRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class SSCService {

    private final SSCRepository sscRepository;

    public SSCService(SSCRepository sscRepository) {
        this.sscRepository = sscRepository;
    }

    public void addSubjectGradeMapping(List<SubjectGpaDto> ssc, SSC newSsc) {

        for (SubjectGpaDto subjectGpa : ssc) {
            SSCSubject subject = SSCSubject.getSubjectByLabel(subjectGpa.getSubject());
            Grade grade = Grade.getSubjectByLabel(subjectGpa.getGpa());
            newSsc.getSubjectGradeMap().put(subject, grade);
        }

        sscRepository.save(newSsc);
    }

    public Grade getGradeForSubject(SSC ssc, SSCSubject subject) {
        return ssc.getSubjectGradeMap().get(subject);
    }

}