package com.hibernate.app.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "course_name")
    private String courseName;

    private String details;

    @ManyToMany(mappedBy = "course")
   // @JoinColumn(name = "student_id")
    private Set<Students> students = new HashSet<Students>();
}
