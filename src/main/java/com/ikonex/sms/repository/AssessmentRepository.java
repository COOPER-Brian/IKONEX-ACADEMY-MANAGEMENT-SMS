package com.ikonex.sms.repository;

import com.ikonex.sms.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    
    // 1. Fetch all marks belonging to a specific student
    List<Assessment> findByStudentId(Long studentId);

    // 2. Fetch all assessments for all students belonging to a specific class stream
    @Query("SELECT a FROM Assessment a WHERE a.student.stream.id = :streamId")
    List<Assessment> findByStreamId(@Param("streamId") Long streamId);
}