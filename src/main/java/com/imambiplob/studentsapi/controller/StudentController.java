package com.imambiplob.studentsapi.controller;

import com.imambiplob.studentsapi.dto.AuthRequest;
import com.imambiplob.studentsapi.dto.ChangePassword;
import com.imambiplob.studentsapi.dto.RegisterStudent;
import com.imambiplob.studentsapi.dto.StudentDashboard;
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

import static com.imambiplob.studentsapi.util.StudentUtil.convertStudentToStudentDashboard;

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
    public ResponseEntity<?> authAndGetToken(@Valid @RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            if(authentication.isAuthenticated())
                return new ResponseEntity<>(jwtService.generateToken(authRequest.getEmail(), (List) studentDetailsService.loadUserByUsername(authRequest.getEmail()).getAuthorities()), HttpStatus.OK);
            else throw new UsernameNotFoundException("Invalid User Request!!!");

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getRole")
    public ResponseEntity<?> getRole(@RequestHeader("Authorization") String header) {
        try {
            if(header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                return new ResponseEntity<>(jwtService.extractRole(token), HttpStatus.OK);
            }

            String message = "Invalid Token!!!";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            String message = "Invalid Token!!!";
            return new ResponseEntity<>(message + " " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getId")
    public ResponseEntity<?> getId(@RequestHeader("Authorization") String header) {
        try {
            if(header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                String email = jwtService.extractUsername(token);
                StudentDashboard student = studentService.getStudentByEmail(email).map(StudentDashboard::new)
                        .orElseThrow(() -> new UsernameNotFoundException("Student not found " + email));
                return new ResponseEntity<>(student.getId(), HttpStatus.OK);
            }

            String message = "Invalid Token!!!";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            String message = "Invalid Token!!!";
            return new ResponseEntity<>(message + " " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addStudent")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> addStudent(@Valid @RequestBody RegisterStudent student) {
        if(studentService.getStudentByEmail(student.getEmail()).isPresent()) {
            return new ResponseEntity<>("This Email is already registered!!! Try Again", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(convertStudentToStudentDashboard(studentService.saveStudent(student)), HttpStatus.CREATED);
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
        if(studentService.getStudentById(id) == null)
            return new ResponseEntity<>("There is no student with this ID!!! Try Again", HttpStatus.BAD_REQUEST);

        if(Objects.equals(jwtAuthFilter.getCurrentUser(), studentService.getStudentById(id).getEmail()) || jwtAuthFilter.isAdmin()) {
            StudentDashboard student = studentService.getStudent(id);
            return new ResponseEntity<>(student, HttpStatus.OK);
        }

        return new ResponseEntity<>("You are not allowed to view this profile", HttpStatus.FORBIDDEN);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudent(@Valid @RequestBody StudentDashboard student, @PathVariable int id) {
        if(studentService.getStudentById(id) == null)
            return new ResponseEntity<>("There is no student with this ID!!! Try Again", HttpStatus.BAD_REQUEST);

        if(Objects.equals(jwtAuthFilter.getCurrentUser(), studentService.getStudentById(id).getEmail()) || jwtAuthFilter.isAdmin()) {
            StudentDashboard updatedStudent = convertStudentToStudentDashboard(studentService.updateStudent(student, id));
            return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
        }

        return new ResponseEntity<>("You are not allowed to update this profile", HttpStatus.FORBIDDEN);
    }

    @PutMapping("/changePassword/{id}")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePassword changePassword, @PathVariable int id) {
        if(studentService.getStudentById(id) == null)
            return new ResponseEntity<>("There is no student with this ID!!! Try Again", HttpStatus.BAD_REQUEST);

        if(Objects.equals(jwtAuthFilter.getCurrentUser(), studentService.getStudentById(id).getEmail()) || jwtAuthFilter.isAdmin()) {
            String message = studentService.changePassword(changePassword, id);
            if(message.equalsIgnoreCase("Password Changed")){
                return new ResponseEntity<>(message, HttpStatus.OK);
            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("You can not change other student's password", HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {
        if(studentService.getStudentById(id) == null)
            return new ResponseEntity<>("There is no student with this ID!!! Try Again", HttpStatus.BAD_REQUEST);

        if (jwtAuthFilter.isAdmin()) {
            String message = studentService.deleteStudent(id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }

        return new ResponseEntity<>("You can not delete student profile!!!", HttpStatus.FORBIDDEN);
    }
}