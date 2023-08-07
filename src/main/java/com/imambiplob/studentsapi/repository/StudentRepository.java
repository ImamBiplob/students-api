package com.imambiplob.studentsapi.repository;

import com.imambiplob.studentsapi.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findByFirstName(String firstName);

    Student findByLastName(String lastName);

    Optional<Student> findByEmail(String email);
}
