package com.imambiplob.studentsapi.util;

import com.imambiplob.studentsapi.dto.StudentDashboard;
import com.imambiplob.studentsapi.dto.SubjectGpaDto;
import com.imambiplob.studentsapi.entity.Student;
import com.imambiplob.studentsapi.enums.Grade;
import com.imambiplob.studentsapi.enums.HSCSubject;
import com.imambiplob.studentsapi.enums.SSCSubject;

import java.util.ArrayList;
import java.util.List;

public class StudentUtil {
    public static StudentDashboard convertStudentToStudentDashboard(Student student) {
        StudentDashboard studentDashboard = new StudentDashboard();
        studentDashboard.setId(student.getId());
        studentDashboard.setFirstName(student.getFirstName());
        studentDashboard.setLastName(student.getLastName());
        studentDashboard.setEmail(student.getEmail());
        studentDashboard.setDob(student.getDob());
        studentDashboard.setAddress(student.getAddress());
        studentDashboard.setBoard(student.getBoard());
        studentDashboard.setContact(student.getContact());

        List<SubjectGpaDto> ssc = new ArrayList<>();
        List<SubjectGpaDto> hsc = new ArrayList<>();

        // Converting SubjectGrade hashmap to List of SubjectGpa objects for ssc
        for(SSCSubject sscSubject: student.getSsc().getSubjectGradeMap().keySet()) {
            String subject = SSCSubject.getLabelBySubject(sscSubject);
            String gpa = Grade.getLabelByGrade(student.getSsc().getSubjectGradeMap().get(sscSubject));
            SubjectGpaDto subjectGpaDto = new SubjectGpaDto();
            subjectGpaDto.setSubject(subject);
            subjectGpaDto.setGpa(gpa);
            ssc.add(subjectGpaDto);
        }

        // Converting SubjectGrade hashmap to List of SubjectGpa objects for hsc
        for(HSCSubject hscSubject: student.getHsc().getSubjectGradeMap().keySet()) {
            String subject = HSCSubject.getLabelBySubject(hscSubject);
            String gpa = Grade.getLabelByGrade(student.getHsc().getSubjectGradeMap().get(hscSubject));
            SubjectGpaDto subjectGpaDto = new SubjectGpaDto();
            subjectGpaDto.setSubject(subject);
            subjectGpaDto.setGpa(gpa);
            hsc.add(subjectGpaDto);
        }

        studentDashboard.setSsc(ssc);
        studentDashboard.setHsc(hsc);

        return studentDashboard;
    }
}