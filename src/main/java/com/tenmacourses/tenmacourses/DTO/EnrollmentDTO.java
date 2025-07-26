package com.tenmacourses.tenmacourses.DTO;

import java.time.LocalDateTime;

public class EnrollmentDTO {
    private LocalDateTime enrolledAt;
    private Integer courseId;
    private Integer userId;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public EnrollmentDTO(LocalDateTime enrolledAt, Integer courseId, Integer userId) {
        this.enrolledAt = enrolledAt;
        this.courseId = courseId;
        this.userId = userId;
    }
}
