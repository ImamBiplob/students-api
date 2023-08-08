package com.imambiplob.studentsapi.service;

import com.imambiplob.studentsapi.dto.RegisterStudent;
import com.imambiplob.studentsapi.dto.StudentDashboard;
import com.imambiplob.studentsapi.dto.SubjectGpaDto;
import com.imambiplob.studentsapi.entity.Student;
import com.imambiplob.studentsapi.enums.Grade;
import com.imambiplob.studentsapi.enums.HSCSubject;
import com.imambiplob.studentsapi.enums.SSCSubject;
import com.imambiplob.studentsapi.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repository;
    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    private static StudentDashboard convertStudentToStudentDashboard(Student student) {
        StudentDashboard studentDashboard = new StudentDashboard();
        studentDashboard.setId(student.getId());
        studentDashboard.setFirstName(student.getFirstName());
        studentDashboard.setLastName(student.getLastName());
        studentDashboard.setEmail(student.getEmail());
        studentDashboard.setContact(student.getContact());
        studentDashboard.setDob(student.getDob());
        studentDashboard.setAddress(student.getAddress());
        studentDashboard.setBoard(student.getBoard());

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

    public Student saveStudent(Student newStudent, RegisterStudent student) {
        newStudent.setFirstName(student.getFirstName());
        newStudent.setLastName(student.getLastName());
        newStudent.setDob(student.getDob());
        newStudent.setEmail(student.getEmail());
        newStudent.setContact(student.getContact());
        newStudent.setAddress(student.getAddress());
        newStudent.setBoard(student.getBoard());

        return repository.save(newStudent);
    }


    public List<StudentDashboard> getStudents() {
        List<Student> students = repository.findAll();
        List<StudentDashboard> studentsDashboard = new ArrayList<>();
        for(Student student: students) {
            StudentDashboard studentDashboard = convertStudentToStudentDashboard(student);
            studentsDashboard.add(studentDashboard);
        }

        return studentsDashboard;
    }

    public StudentDashboard getStudent(int id) {
        Student student = repository.findById(id).orElse(null);
        assert student != null;
        return convertStudentToStudentDashboard(student);
    }

    public Student getStudentById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Student getStudentByFirstName(String firstName) {
        return repository.findByFirstName(firstName);
    }

    public Student getStudentByLastName(String lastName) {
        return repository.findByLastName(lastName);
    }

    public String deleteStudent(int id) {
        Student student = repository.findById(id).orElse(null);
        assert student != null;
        String name = student.getFirstName();
        repository.deleteById(id);
        return "Student Profile Deleted of ID: " + id + " and Name: " + name;
    }

    public Student updateStudent(Student existingStudent, RegisterStudent student) {
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setDob(student.getDob());
        existingStudent.setBoard(student.getBoard());
        existingStudent.setContact(student.getContact());
        existingStudent.setAddress(student.getAddress());

        return repository.save(existingStudent);
    }
}