package com.tenmacourses.tenmacourses.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lessons")
public class Lessons {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Courses course;

    private String title;

    private String content_type;

    private String content_url;

    private String duration;

    private LocalDateTime created_at;

    public Lessons(int id, Courses course, String title, String content_type, String content_url, String duration, LocalDateTime created_at) {
        Id = id;
        this.course = course;
        this.title = title;
        this.content_type = content_type;
        this.content_url = content_url;
        this.duration = duration;
        this.created_at = created_at;
    }

    public Lessons() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
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

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Lessons{" +
                "Id=" + Id +
                ", course=" + course +
                ", title='" + title + '\'' +
                ", content_type='" + content_type + '\'' +
                ", content_url='" + content_url + '\'' +
                ", duration='" + duration + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
