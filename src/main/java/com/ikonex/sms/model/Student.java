package com.ikonex.sms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String admissionNumber;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    // Many students belong to one stream
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stream_id")
    @ToString.Exclude // To prevent circular reference in toString()
    private StreamEntity stream;
}