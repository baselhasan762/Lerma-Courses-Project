package com.tenmacourses.tenmacourses.DTO;

import com.tenmacourses.tenmacourses.Entity.Courses;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;


public class LessonResponseDTO {
    public LessonResponseDTO(Integer courseId, String title, String content_type, String content_url, String duration, LocalDate createdAt) {
        this.courseId = courseId;
        this.title = title;
        this.content_type = content_type;
        this.content_url = content_url;
        this.duration = duration;
        this.createdAt = createdAt;
    }

    private Integer courseId;
    private String title;

    private String content_type;
    private String content_url;
    private String duration;
    private LocalDate createdAt;

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCourseId() {
        return courseId;
    }


    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
