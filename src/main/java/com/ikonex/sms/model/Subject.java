package com.ikonex.sms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "subjects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code; // e.g., MATH101, ENG202

    // Many-to-Many connection: Multiple streams can take multiple subjects
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "stream_subjects",
        joinColumns = @JoinColumn(name = "subject_id"),
        inverseJoinColumns = @JoinColumn(name = "stream_id")
    )
    @ToString.Exclude
    private List<StreamEntity> streams;
}