package com.imambiplob.studentsapi.controller;

import com.imambiplob.studentsapi.dto.AuthRequest;
import com.imambiplob.studentsapi.dto.ChangePassword;
import com.imambiplob.studentsapi.dto.RegisterStudent;
import com.imambiplob.studentsapi.dto.StudentDashboard;
import com.imambiplob.studentsapi.enums.Grade;
import com.imambiplob.studentsapi.enums.HSCSubject;
import com.imambiplob.studentsapi.enums.SSCSubject;
import com.imambiplob.studentsapi.exception.EmailAlreadyTakenException;
import com.imambiplob.studentsapi.exception.IllegalActionException;
import com.imambiplob.studentsapi.exception.StudentNotFoundException;
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
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        if(authentication.isAuthenticated())
            return new ResponseEntity<>(jwtService.generateToken(authRequest.getEmail(),
                    (List) studentDetailsService.loadUserByUsername(authRequest.getEmail()).getAuthorities()), HttpStatus.OK);

        else throw new UsernameNotFoundException("Invalid User Request!!!");
    }

    @GetMapping("/getRole")
    public ResponseEntity<?> getRole(@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(jwtService.extractRole(token.substring(7)), HttpStatus.OK);
    }

    @GetMapping("/getId")
    public ResponseEntity<?> getId(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.substring(7));
        StudentDashboard student = studentService.getStudentByEmail(email).map(StudentDashboard::new)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid User Request!!! " + email));
        return new ResponseEntity<>(student.getId(), HttpStatus.OK);
    }

    @PostMapping("/addStudent")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> addStudent(@Valid @RequestBody RegisterStudent student) throws EmailAlreadyTakenException {
        if(studentService.getStudentByEmail(student.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException(student.getEmail() + " is already registered!!! Try Again");
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
    public ResponseEntity<?> getStudentById(@PathVariable int id) throws StudentNotFoundException, IllegalActionException {
        if(studentService.getStudentById(id) == null)
            throw new StudentNotFoundException("There is no student with ID " + id + "!!!" + "Try Again");

        if(Objects.equals(jwtAuthFilter.getCurrentUser(), studentService.getStudentById(id).getEmail()) || jwtAuthFilter.isAdmin()) {
            StudentDashboard student = studentService.getStudent(id);
            return new ResponseEntity<>(student, HttpStatus.OK);
        }

        throw new IllegalActionException("You are not allowed to view this profile!!!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudent(@Valid @RequestBody StudentDashboard student, @PathVariable int id) throws StudentNotFoundException, IllegalActionException {
        if(studentService.getStudentById(id) == null)
            throw new StudentNotFoundException("There is no student with ID " + id + "!!!" + "Try Again");

        if(Objects.equals(jwtAuthFilter.getCurrentUser(), studentService.getStudentById(id).getEmail()) || jwtAuthFilter.isAdmin()) {
            StudentDashboard updatedStudent = convertStudentToStudentDashboard(studentService.updateStudent(student, id));
            return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
        }

        throw new IllegalActionException("You are not allowed to update this profile!!!");
    }

    @PutMapping("/changePassword/{id}")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePassword changePassword, @PathVariable int id) throws StudentNotFoundException, IllegalActionException {
        if(studentService.getStudentById(id) == null)
            throw new StudentNotFoundException("There is no student with ID " + id + "!!!" + "Try Again");

        if(Objects.equals(jwtAuthFilter.getCurrentUser(), studentService.getStudentById(id).getEmail()) || jwtAuthFilter.isAdmin()) {
            String message = studentService.changePassword(changePassword, id);
            if(message.equalsIgnoreCase("Password Changed")){
                return new ResponseEntity<>(message, HttpStatus.OK);
            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        throw new IllegalActionException("You can not change other student's password!!!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) throws StudentNotFoundException, IllegalActionException {
        if(studentService.getStudentById(id) == null)
            throw new StudentNotFoundException("There is no student with ID " + id + "!!!" + "Try Again");

        if (jwtAuthFilter.isAdmin()) {
            String message = studentService.deleteStudent(id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }

        throw new IllegalActionException("You can not delete student profile!!!");
    }
}