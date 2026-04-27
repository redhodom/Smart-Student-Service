package com.sm.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sm.entity.Student;
import com.sm.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ✅ REGISTER
    public Student register(Student student) {

        if (repository.findByEmail(student.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // 🔐 encrypt password
        student.setPassword(passwordEncoder.encode(student.getPassword()));

        return repository.save(student);
    }

    // ✅ LOGIN
    public String login(String email, String password) {

        Student student = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (passwordEncoder.matches(password, student.getPassword())) {
            return "Login successful";
        } else {
            return "Invalid password";
        }
    }

    // ✅ GET ALL
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    // ✅ GET BY ID
    public Student getStudentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // ✅ UPDATE (FINAL CLEAN VERSION)
    public Student updateStudent(Long id, Student updatedStudent) {

        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // 🧠 update only if values exist (prevents null overwrite)

        if (updatedStudent.getName() != null) {
            student.setName(updatedStudent.getName());
        }

        if (updatedStudent.getEmail() != null) {
            student.setEmail(updatedStudent.getEmail());
        }

        if (updatedStudent.getCourse() != null) {
            student.setCourse(updatedStudent.getCourse());
        }

        // 🔐 update password only if provided
        if (updatedStudent.getPassword() != null && !updatedStudent.getPassword().isEmpty()) {
            student.setPassword(passwordEncoder.encode(updatedStudent.getPassword()));
        }

        // 🖼️ update photo only if provided
        if (updatedStudent.getPhoto() != null && !updatedStudent.getPhoto().isEmpty()) {
            student.setPhoto(updatedStudent.getPhoto());
        }

        return repository.save(student);
    }

    // ✅ DELETE
    public void deleteStudent(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Student not found");
        }

        repository.deleteById(id);
    }
    
    public Student getStudentByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }
}