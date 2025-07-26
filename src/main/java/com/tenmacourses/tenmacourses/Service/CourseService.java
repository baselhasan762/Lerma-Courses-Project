package com.tenmacourses.tenmacourses.Service;

import com.tenmacourses.tenmacourses.DTO.CourseRequestDTO;
import com.tenmacourses.tenmacourses.DTO.CourseResponseDTO;
import com.tenmacourses.tenmacourses.Entity.Courses;
import com.tenmacourses.tenmacourses.Entity.Users;
import com.tenmacourses.tenmacourses.Repository.CourseRepo;
import com.tenmacourses.tenmacourses.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepo courseRepo;
    private final UserRepo userRepo;

    @Autowired
    public CourseService(CourseRepo courseRepo,UserRepo userRepo) {
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
    }

    public List<CourseResponseDTO> getAllCourses() {
        return courseRepo.findAll().stream()
                .map(course -> {
                    CourseResponseDTO dto = new CourseResponseDTO();
                    dto.setId(course.getId());
                    dto.setCourseName(course.getCourseName());
                    dto.setCourseDescription(course.getCourseDescription());
                    dto.setPrice(course.getPrice());
                    dto.setCreatedAt(course.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Optional<Courses> getCourseById(Integer id) {
        return courseRepo.findById(id);
    }



    public boolean addNewCourse(CourseRequestDTO courseDto) {
        try {
            Courses course = new Courses();
            course.setCourseName(courseDto.getCourseName());
            Users instrucotr = userRepo.findById(courseDto.getInstructor_id()).orElseThrow(()->new RuntimeException("Instructor not found"));
            course.setInstructor(instrucotr);



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

    public Courses getCourseByName(String courseName) {
        return courseRepo.findByCourseName(courseName);

    }
}
