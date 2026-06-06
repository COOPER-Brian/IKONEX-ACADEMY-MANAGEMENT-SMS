package com.ikonex.sms.service;

import com.ikonex.sms.model.Student;
import com.ikonex.sms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // View all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // View a single student's details
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    // Register a new student or update an existing one
    public String saveStudent(Student student) {
        // Only check for duplicate admission numbers if it's a new student registration
        if (student.getId() == null && studentRepository.existsByAdmissionNumber(student.getAdmissionNumber())) {
            return "Error: Admission number already exists!";
        }
        studentRepository.save(student);
        return "Success";
    }

    // Delete a student
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // View students belonging to a specific class stream
    public List<Student> getStudentsByStream(Long streamId) {
        return studentRepository.findByStreamId(streamId);
    }
}