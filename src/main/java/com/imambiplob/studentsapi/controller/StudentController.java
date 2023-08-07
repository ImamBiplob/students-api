package com.imambiplob.studentsapi.controller;

import com.imambiplob.studentsapi.dto.AuthRequest;
import com.imambiplob.studentsapi.dto.RegisterStudent;
import com.imambiplob.studentsapi.dto.StudentDashboard;
import com.imambiplob.studentsapi.entity.HSC;
import com.imambiplob.studentsapi.entity.SSC;
import com.imambiplob.studentsapi.entity.Student;
import com.imambiplob.studentsapi.enums.Grade;
import com.imambiplob.studentsapi.enums.HSCSubject;
import com.imambiplob.studentsapi.enums.SSCSubject;
import com.imambiplob.studentsapi.service.HSCService;
import com.imambiplob.studentsapi.service.JwtService;
import com.imambiplob.studentsapi.service.SSCService;
import com.imambiplob.studentsapi.service.StudentService;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;
    private final SSCService sscService;
    private final HSCService hscService;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public StudentController(StudentService studentService, PasswordEncoder passwordEncoder, SSCService sscService, HSCService hscService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.studentService = studentService;
        this.passwordEncoder = passwordEncoder;
        this.sscService = sscService;
        this.hscService = hscService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/authenticate")
    public String authAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getContact()));
        if(authentication.isAuthenticated())
            return jwtService.generateToken(authRequest.getEmail());
        else throw new UsernameNotFoundException("Invalid User Request!!!");

    }
    @PostMapping("/addStudent")
    public Student addStudent(@Valid @RequestBody RegisterStudent student) {
        student.setContact(passwordEncoder.encode(student.getContact()));
        Student newStudent = new Student();
        newStudent.setFirstName(student.getFirstName());
        newStudent.setLastName(student.getLastName());
        newStudent.setDob(student.getDob());
        newStudent.setEmail(student.getEmail());
        newStudent.setContact(student.getContact());
        newStudent.setAddress(student.getAddress());
        newStudent.setBoard(student.getBoard());
        newStudent.setSsc(new SSC());
        newStudent.setHsc(new HSC());
        sscService.addSubjectGradeMapping(student.getSsc(), newStudent.getSsc());
        hscService.addSubjectGradeMapping(student.getHsc(), newStudent.getHsc());

        return studentService.saveStudent(newStudent);
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
    @PreAuthorize("hasAuthority('Dhaka')")
    public List<StudentDashboard> getAllStudents() {
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

    @PutMapping("/update/{id}")
    public Student updateStudent(@Valid @RequestBody RegisterStudent student, @PathVariable int id) {
        student.setContact(passwordEncoder.encode(student.getContact()));
        Student existingStudent = studentService.getStudentById(id);
        sscService.addSubjectGradeMapping(student.getSsc(), existingStudent.getSsc());
        hscService.addSubjectGradeMapping(student.getHsc(), existingStudent.getHsc());
        return studentService.updateStudent(existingStudent, student);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id) {
        return studentService.deleteStudent(id);
    }
}