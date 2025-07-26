package com.tenmacourses.tenmacourses.Entity;

import com.tenmacourses.tenmacourses.DTO.CourseRequestDTO;
import com.tenmacourses.tenmacourses.DTO.CourseResponseDTO;
import com.tenmacourses.tenmacourses.Entity.Courses;
import com.tenmacourses.tenmacourses.Entity.Users;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Enrollment {

    public Enrollment(LocalDateTime enrolledAt, Integer courseId, Courses course, Integer userId, Users user) {
        this.enrolledAt = enrolledAt;
        this.courseId = courseId;
        this.course = course;
        this.userId = userId;
        this.user = user;
    }


    public Enrollment(){};
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name ="enroll_date")
    private LocalDateTime enrolledAt;

    @Column(name="course_id")
    private Integer courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_id", insertable = false, updatable = false)
    private Courses course;

    @Column(name="user_id")
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", insertable = false, updatable = false)
    private Users user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
