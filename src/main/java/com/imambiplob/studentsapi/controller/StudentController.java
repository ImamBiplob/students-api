package com.imambiplob.studentsapi.controller;

import com.imambiplob.studentsapi.entity.Student;
import com.imambiplob.studentsapi.enums.Grade;
import com.imambiplob.studentsapi.enums.HSCSubject;
import com.imambiplob.studentsapi.enums.SSCSubject;
import com.imambiplob.studentsapi.service.HSCService;
import com.imambiplob.studentsapi.service.SSCService;
import com.imambiplob.studentsapi.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {
    private final StudentService studentService;
    private final SSCService sscService;
    private final HSCService hscService;

    public StudentController(StudentService studentService, SSCService sscService, HSCService hscService) {
        this.studentService = studentService;
        this.sscService = sscService;
        this.hscService = hscService;
    }

    @PostMapping("/addStudent")
    public Student addStudent(@Valid @RequestBody Student student) {
        sscService.addSubjectGradeMapping(student.getSsc(), student.getSsc().getSscSubjects(), student.getSsc().getSscGrades());
        hscService.addSubjectGradeMapping(student.getHsc(), student.getHsc().getHscSubjects(), student.getHsc().getHscGrades());
        return studentService.saveStudent(student);
    }

    @PostMapping("/addStudents")
    public List<Student> addStudents(@Valid @RequestBody List<Student> students) {
        return studentService.saveStudents(students);
    }
    @GetMapping("/SSCSubjects")
    public List<String> getSSCSubjects() {
        List<String> sscSubjectList = new ArrayList<>();
        for(SSCSubject subject: SSCSubject.values()) {
            sscSubjectList.add(SSCSubject.getLabelBySubject(subject));
        }
        return sscSubjectList;
    }

    @GetMapping("/HSCSubjects")
    public List<String> getHSCSubjects() {
        List<String> hscSubjectList = new ArrayList<>();
        for(HSCSubject subject: HSCSubject.values()) {
            hscSubjectList.add(HSCSubject.getLabelBySubject(subject));
        }
        return hscSubjectList;
    }

    @GetMapping("/Grades")
    public List<String> getGrades() {
        List<String> gradeList = new ArrayList<>();
        for (Grade grade: Grade.values()) {
            gradeList.add(Grade.getLabelByGrade(grade));
        }
        return gradeList;
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentService.getStudents();
    }

    @GetMapping("/studentById/{id}")
    public Student getStudentById(@PathVariable int id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("/studentByFirstName/{firstName}")
    public Student getStudentByFirstName(@PathVariable String firstName) {
        return studentService.getStudentByFirstName(firstName);
    }

    @GetMapping("/studentByLastName/{lastName}")
    public Student getStudentByLastName(@PathVariable String lastName) {
        return studentService.getStudentByLastName(lastName);
    }

    @PutMapping("/update")
    public Student updateStudent(@Valid @RequestBody Student student) {
        sscService.addSubjectGradeMapping(student.getSsc(), student.getSsc().getSscSubjects(), student.getSsc().getSscGrades());
        hscService.addSubjectGradeMapping(student.getHsc(), student.getHsc().getHscSubjects(), student.getHsc().getHscGrades());
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id) {
        return studentService.deleteStudent(id);
    }
}