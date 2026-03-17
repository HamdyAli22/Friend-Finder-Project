package com.hibernate.app.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "students")
public class Students {
    @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "students_seq_gen")
     @SequenceGenerator(name = "students_seq_gen", sequenceName = "students_seq", allocationSize = 1)
    private Integer id;

    private String username;
    private String password;

   /* @OneToMany(mappedBy = "students")
    private Set<Emails> emails = new HashSet<Emails>();*/

   @OneToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "std_detail_id",unique = true)
   private StudentDetails studentDetails;

   /*@ManyToMany
 //  @JoinColumn(name = "course_id")
    private Set<Course> course = new HashSet<Course>();*/

    public Students() {

    }

    public Students(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StudentDetails getStudentDetails() {
        return studentDetails;
    }

    public void setStudentDetails(StudentDetails studentDetails) {
        this.studentDetails = studentDetails;
    }
}
