package com.ikonex.sms.repository;

import com.ikonex.sms.model.StreamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamRepository extends JpaRepository<StreamEntity, Long> {
    // This will help us check if a stream name already exists
    boolean existsByName(String name);
}