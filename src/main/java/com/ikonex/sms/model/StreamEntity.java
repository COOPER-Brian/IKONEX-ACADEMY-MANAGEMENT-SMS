package com.ikonex.sms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import lombok.ToString;

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

    // One stream can have many students. We will link this up fully when we create the Student model.
    @OneToMany(mappedBy = "stream", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude // To prevent circular reference in toString()
    private List<Student> students;
}