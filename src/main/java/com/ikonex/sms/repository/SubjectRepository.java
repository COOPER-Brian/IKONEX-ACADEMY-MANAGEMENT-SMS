package com.ikonex.sms.repository;

import com.ikonex.sms.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByCode(String code);
}