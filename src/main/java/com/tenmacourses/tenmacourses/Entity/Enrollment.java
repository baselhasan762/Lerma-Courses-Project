package com.tenmacourses.tenmacourses.Entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name ="enroll_date")
    private LocalDateTime enrolledAt;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="course_id")
    private Courses course;

}
