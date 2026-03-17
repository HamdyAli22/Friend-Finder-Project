package com.hibernate.app.model;

import javax.persistence.*;

@Entity
public class Emails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emails_seq_gen")
    @SequenceGenerator(name = "emails_seq_gen", sequenceName = "emails_seq", allocationSize = 1)
    private int id;
    private String content;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Students students;

    public Emails() {
    }

    public Emails(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}



