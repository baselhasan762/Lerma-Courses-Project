package com.tenmacourses.tenmacourses.DTO;

import com.tenmacourses.tenmacourses.Entity.Courses;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class LessonDTO {
    @NotNull(message = "Course ID is required")
    private Courses course;

    @NotBlank(message = "Title is required")
    private Integer courseId;

    @NotBlank(message = "Content type is required")
    private String title;

    private String content_type;
    private String content_url;
    private String duration;

    public Integer getCourseId() {
        return courseId;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
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
