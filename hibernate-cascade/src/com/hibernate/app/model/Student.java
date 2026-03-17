package com.hibernate.app.model;

import javax.persistence.*;

@Entity
@Table(name = "student")
public class Student {
    @Id
  //  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq_gen")
   // @SequenceGenerator(name = "student_seq_gen", sequenceName = "student_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "student_id")
    Integer id;

    @Column(name = "student_name",nullable = false , unique = true,length = 1000)
    String name;

    String address;
    String phone;

    public Student() {
    }

    public Student(Integer id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Student(String name, String address, String phone) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return  "Student[id= " + id + ",name= " + name + ",address= " + address + ",phone= " + phone + "]";
    }
}
