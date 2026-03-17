package com.hibernate.app.model;

import javax.persistence.*;

@Entity
@Table(name = "student_details")
public class StudentDetails {
    @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_details_seq_gen")
     @SequenceGenerator(name = "student_details_seq_gen", sequenceName = "student_details_seq", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "stu_dtl_id")
    Integer id;

    @Column(name = "student_name",nullable = false , unique = true,length = 1000)
    String name;

    String address;
    String phone;

    @OneToOne(mappedBy = "studentDetails")
    private Students students;

    public StudentDetails() {
    }

    public StudentDetails(Integer id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public StudentDetails(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Students getStudents() {
        return students;
    }

    public void setStudents(Students students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return  "Student[id= " + id + ",name= " + name + ",address= " + address + ",phone= " + phone + "]";
    }
}




