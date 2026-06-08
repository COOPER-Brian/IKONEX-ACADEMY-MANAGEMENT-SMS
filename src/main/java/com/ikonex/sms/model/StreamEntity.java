package com.ikonex.sms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "streams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // One stream can have many students.
    @OneToMany(mappedBy = "stream", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude 
    private List<Student> students;

    //  Connects back to the 'streams' field inside Subject entity
    @ManyToMany(mappedBy = "streams")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Subject> subjects = new HashSet<>();
}