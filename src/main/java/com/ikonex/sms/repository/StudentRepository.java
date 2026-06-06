package com.ikonex.sms.repository;

import com.ikonex.sms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Check if an admission number is already taken
    boolean existsByAdmissionNumber(String admissionNumber);
    
    // Find all students belonging to a specific stream (Requirement rule!)
    List<Student> findByStreamId(Long streamId);
}