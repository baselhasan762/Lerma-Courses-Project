package com.tenmacourses.tenmacourses.Repository;

import com.tenmacourses.tenmacourses.Entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepo extends JpaRepository<Reviews,Integer> {
}
