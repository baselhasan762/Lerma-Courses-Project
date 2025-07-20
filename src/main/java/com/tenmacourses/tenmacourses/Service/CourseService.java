package com.tenmacourses.tenmacourses.Service;

import com.tenmacourses.tenmacourses.Entity.Courses;
import com.tenmacourses.tenmacourses.Repository.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepo courseRepo;

    @Autowired
    public CourseService(CourseRepo courseRepo) {
        this.courseRepo = courseRepo;
    }

    public List<Courses> getAllCourses() {
        return courseRepo.findAll();
    }

    public Optional<Courses> getCourseById(Integer id) {
        return courseRepo.findById(id);
    }

    public boolean addNewCourse(Courses course) {
        try {
            courseRepo.save(course);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateCourse(Integer id, Courses updatedCourse) {
        return courseRepo.findById(id).map(existingCourse -> {
            existingCourse.setCourseName(updatedCourse.getCourseName());
            existingCourse.setCourseDescription(updatedCourse.getCourseDescription());
            existingCourse.setInstructor(updatedCourse.getInstructor());
            existingCourse.setCreatedAt(updatedCourse.getCreatedAt());
            existingCourse.setUpdatedAt(updatedCourse.getUpdatedAt());

            courseRepo.save(existingCourse);
            return true;
        }).orElse(false);
    }

    public boolean deleteCourse(Integer id) {
        try {
            courseRepo.deleteById(id);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
