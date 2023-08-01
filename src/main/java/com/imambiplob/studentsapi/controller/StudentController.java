package com.imambiplob.studentsapi.controller;

import com.imambiplob.studentsapi.entity.Student;
import com.imambiplob.studentsapi.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping("/addStudent")
    public Student addStudent(@Valid @RequestBody Student student) {
        return service.saveStudent(student);
    }

    @PostMapping("/addStudents")
    public List<Student> addStudents(@Valid @RequestBody List<Student> students) {
        return service.saveStudents(students);
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return service.getStudents();
    }

    @GetMapping("/studentById/{id}")
    public Student getStudentById(@PathVariable int id) {
        return service.getStudentById(id);
    }

    @GetMapping("/studentByFirstName/{firstName}")
    public Student getStudentByFirstName(@PathVariable String firstName) {
        return service.getStudentByFirstName(firstName);
    }

    @GetMapping("/studentByLastName/{lastName}")
    public Student getStudentByLastName(@PathVariable String lastName) {
        return service.getStudentByLastName(lastName);
    }

    @PutMapping("/update")
    public Student updateStudent(@RequestBody Student student) {
        return service.updateStudent(student);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id) {
        return service.deleteStudent(id);
    }
}
