package com.tenmacourses.tenmacourses.Repository;

import com.tenmacourses.tenmacourses.Entity.Lessons;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonsRepo extends JpaRepository<Lessons,Integer> {
}
