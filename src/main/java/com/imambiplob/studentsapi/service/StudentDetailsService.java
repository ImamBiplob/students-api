package com.imambiplob.studentsapi.service;

import com.imambiplob.studentsapi.config.StudentDetails;
import com.imambiplob.studentsapi.entity.Student;
import com.imambiplob.studentsapi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StudentDetailsService implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Student> student = studentRepository.findByEmail(username);

        return student.map(StudentDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found " + username));
    }
}
