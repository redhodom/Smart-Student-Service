package com.sm.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sm.entity.Student;
import com.sm.services.StudentService;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:5173") // frontend URL
public class StudentController {

    @Autowired
    private StudentService service;

    private static final String UPLOAD_DIR = "uploads/";
    
    @GetMapping("/email/{email}")
    public Student getByEmail(@PathVariable String email) {
        return service.getStudentByEmail(email);
    }

    // ✅ CREATE (JSON)
    @PostMapping("/register")
    public ResponseEntity<Student> register(@RequestBody Student student) {
        Student saved = service.register(student);
        return ResponseEntity.ok(saved);
    }

    // ✅ READ ALL
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(service.getAllStudents());
    }

    // ✅ READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStudentById(id));
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student student
    ) {
        return ResponseEntity.ok(service.updateStudent(id, student));
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
        return ResponseEntity.ok("Student deleted successfully");
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Student student) {
        String result = service.login(student.getEmail(), student.getPassword());
        return ResponseEntity.ok(result);
    }

    // ✅ IMAGE UPLOAD
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = "C:/student_uploads/"; // 🔥 use absolute path

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String path = uploadDir + fileName;

            System.out.println("Saving to: " + path);

            file.transferTo(new File(path));

            return fileName;

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 THIS IS THE KEY
            return "Upload failed";
        }
    }
    
    @PostMapping("/admin/login")
    public String adminLogin(@RequestBody Student student) {

        if (student.getEmail().equals("admin@gmail.com") &&
            student.getPassword().equals("admin123")) {
            return "Admin login successful";
        }

        return "Invalid admin credentials";
    }
    
}