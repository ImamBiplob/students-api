package com.imambiplob.studentsapi.service;

import com.imambiplob.studentsapi.dto.RegisterStudent;
import com.imambiplob.studentsapi.dto.StudentDashboard;
import com.imambiplob.studentsapi.entity.Student;
import com.imambiplob.studentsapi.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.imambiplob.studentsapi.util.StudentUtil.convertStudentToStudentDashboard;

@Service
public class StudentService {
    private final StudentRepository repository;
    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student saveStudent(Student newStudent, RegisterStudent student) {
        newStudent.setFirstName(student.getFirstName());
        newStudent.setLastName(student.getLastName());
        newStudent.setDob(student.getDob());
        newStudent.setEmail(student.getEmail());
        newStudent.setPassword(student.getPassword());
        newStudent.setAddress(student.getAddress());
        newStudent.setBoard(student.getBoard());
        newStudent.setContact(student.getContact());

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
        existingStudent.setPassword(student.getPassword());
        existingStudent.setAddress(student.getAddress());
        existingStudent.setContact(student.getContact());

        return repository.save(existingStudent);
    }
}