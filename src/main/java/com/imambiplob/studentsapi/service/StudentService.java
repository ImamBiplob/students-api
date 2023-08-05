package com.imambiplob.studentsapi.service;

import com.imambiplob.studentsapi.entity.Student;
import com.imambiplob.studentsapi.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repository;
    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student saveStudent(Student student) { return repository.save(student);}

    public List<Student> saveStudents(List<Student> students) {
        return repository.saveAll(students);
    }

    public List<Student> getStudents() {
        return repository.findAll();
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

    public Student updateStudent(Student student) {
        Student existingStudent = repository.findById(student.getId()).orElse(null);
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setDob(student.getDob());
        existingStudent.setNationality(student.getNationality());
        existingStudent.setContact(student.getContact());
        existingStudent.setAddress(student.getAddress());
        existingStudent.setSsc(student.getSsc());
        existingStudent.setHsc(student.getHsc());

        return repository.save(existingStudent);
    }
}