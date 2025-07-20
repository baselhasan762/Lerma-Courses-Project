package com.tenmacourses.tenmacourses.Repository;

import com.tenmacourses.tenmacourses.Entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepo extends JpaRepository<Enrollment,Integer> {
}
