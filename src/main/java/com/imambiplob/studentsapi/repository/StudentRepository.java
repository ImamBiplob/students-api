package com.imambiplob.studentsapi.repository;

import com.imambiplob.studentsapi.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findByFirstName(String firstName);

    Student findByLastName(String lastName);
}
