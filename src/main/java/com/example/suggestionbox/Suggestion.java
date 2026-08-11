package com.example.suggestionbox;

import jakarta.persistence.*;

@Entity
@Table(name = "suggestions")
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentInfo;

    @Column(columnDefinition = "TEXT")
    private String content;

    public Suggestion() {}

    public Suggestion(String studentInfo, String content) {
        this.studentInfo = studentInfo;
        this.content = content;
    }

    public Long getId() { return id; }
    public String getStudentInfo() { return studentInfo; }
    public String getContent() { return content; }
}
