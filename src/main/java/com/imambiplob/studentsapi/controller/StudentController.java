package com.imambiplob.studentsapi.controller;

import com.imambiplob.studentsapi.dto.AuthRequest;
import com.imambiplob.studentsapi.dto.RegisterStudent;
import com.imambiplob.studentsapi.dto.StudentDashboard;
import com.imambiplob.studentsapi.entity.Student;
import com.imambiplob.studentsapi.enums.Grade;
import com.imambiplob.studentsapi.enums.HSCSubject;
import com.imambiplob.studentsapi.enums.SSCSubject;
import com.imambiplob.studentsapi.filter.JwtAuthFilter;
import com.imambiplob.studentsapi.service.JwtService;
import com.imambiplob.studentsapi.service.StudentDetailsService;
import com.imambiplob.studentsapi.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class StudentController {
    private final StudentService studentService;
    private final JwtService jwtService;
    private final JwtAuthFilter jwtAuthFilter;
    private final StudentDetailsService studentDetailsService;
    private final AuthenticationManager authenticationManager;

    public StudentController(StudentService studentService, JwtService jwtService, JwtAuthFilter jwtAuthFilter, StudentDetailsService studentDetailsService, AuthenticationManager authenticationManager) {
        this.studentService = studentService;
        this.jwtService = jwtService;
        this.jwtAuthFilter = jwtAuthFilter;
        this.studentDetailsService = studentDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/authenticate")
    public String authAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if(authentication.isAuthenticated())
            return jwtService.generateToken(authRequest.getEmail(), (List) studentDetailsService.loadUserByUsername(authRequest.getEmail()).getAuthorities());
        else throw new UsernameNotFoundException("Invalid User Request!!!");
    }

    @GetMapping("/getRole")
    public List getRole(@RequestHeader("Authorization") String header) {
        if(header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            return jwtService.extractRole(token);
        }
        return null;
    }

    @GetMapping("/getId")
    public ResponseEntity<?> getId(@RequestHeader("Authorization") String header) {
        if(header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            String email = jwtService.extractUsername(token);
            StudentDashboard student = studentService.getStudentByEmail(email).map(StudentDashboard::new)
                    .orElseThrow(() -> new UsernameNotFoundException("Student not found " + email));
            return new ResponseEntity<>(student.getId(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addStudent")
    @PreAuthorize("hasAuthority('Admin')")
    public Student addStudent(@Valid @RequestBody RegisterStudent student) {
        return studentService.saveStudent(student);
    }

    @GetMapping("/SSCSubjects")
    public List<String> getSSCSubjects() {
        return SSCSubject.getAllLabels();
    }

    @GetMapping("/HSCSubjects")
    public List<String> getHSCSubjects() {
        return HSCSubject.getAllLabels();
    }

    @GetMapping("/Grades")
    public List<String> getGrades() {
        return Grade.getAllLabels();
    }

    @GetMapping("/students")
    @PreAuthorize("hasAuthority('Admin')")
    public List<StudentDashboard> getAllStudents() {
        return studentService.getStudents();
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable int id) {

        if(Objects.equals(jwtAuthFilter.getCurrentUser(), studentService.getStudentById(id).getEmail()) || jwtAuthFilter.isAdmin()) {
            StudentDashboard student = studentService.getStudent(id);
            return new ResponseEntity<>(student, HttpStatus.OK);
        }

        String message = "You are not allowed to view this profile";
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
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
    public ResponseEntity<?> updateStudent(@Valid @RequestBody RegisterStudent student, @PathVariable int id) {

        if(Objects.equals(jwtAuthFilter.getCurrentUser(), studentService.getStudentById(id).getEmail()) || jwtAuthFilter.isAdmin()) {
            Student updatedStudent = studentService.updateStudent(student, id);
            return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
        }

        String message = "You are not allowed to update this profile";
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {

        if (Objects.equals(jwtAuthFilter.getCurrentUser(), studentService.getStudentById(id).getEmail()) || jwtAuthFilter.isAdmin()) {
            String message = studentService.deleteStudent(id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }

        String message = "You can not delete other student's profile!!!";
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }
}