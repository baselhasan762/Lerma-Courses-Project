package com.tenmacourses.tenmacourses.Repository;

import com.tenmacourses.tenmacourses.Entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepo extends JpaRepository<Courses,Integer> {
}
