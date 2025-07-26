package com.tenmacourses.tenmacourses.Repository;

import com.tenmacourses.tenmacourses.Entity.Lessons;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonsRepo extends JpaRepository<Lessons,Integer> {
    Lessons findByTitle(String title);
}
