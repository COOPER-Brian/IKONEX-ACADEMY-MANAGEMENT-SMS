package com.ikonex.sms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "assessments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private Double marksObtained;

    @Column(nullable = false)
    private Double maxMarks; // Usually 100

    // Simple automatic helper to calculate the exact percentage score
    public Double getPercentage() {
        if (maxMarks == null || maxMarks == 0) return 0.0;
        return (marksObtained / maxMarks) * 100;
    }
}