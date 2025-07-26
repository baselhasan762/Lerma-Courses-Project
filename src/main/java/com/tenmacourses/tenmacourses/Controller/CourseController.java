package com.tenmacourses.tenmacourses.Controller;

import com.tenmacourses.tenmacourses.DTO.CourseRequestDTO;
import com.tenmacourses.tenmacourses.DTO.CourseResponseDTO;
import com.tenmacourses.tenmacourses.DTO.UserResponseDTO;
import com.tenmacourses.tenmacourses.Entity.Courses;
import com.tenmacourses.tenmacourses.Entity.Enrollment;
import com.tenmacourses.tenmacourses.Entity.UserPrincipal;
import com.tenmacourses.tenmacourses.Entity.Users;
import com.tenmacourses.tenmacourses.Enums.Role;
import com.tenmacourses.tenmacourses.Service.CourseService;
import com.tenmacourses.tenmacourses.Service.EnrollmentService;
import com.tenmacourses.tenmacourses.Service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final EnrollmentService enrollmentService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService,EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.userService = userService;
        this.enrollmentService=enrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        List<CourseResponseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Integer id) {
        return courseService.getCourseById(id)
                .map(course -> {
                    CourseResponseDTO dto = new CourseResponseDTO();
                    dto.setId(course.getId());
                    dto.setCourseName(course.getCourseName());
                    dto.setCourseDescription(course.getCourseDescription());
                    dto.setPrice(course.getPrice());
                    dto.setCreatedAt(course.getCreatedAt());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<String> addNewCourse(@RequestBody CourseRequestDTO course) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.getUserByName(username);

        if(!user.getId().equals(course.getInstructor_id())){
            return ResponseEntity.status(403).body("you must be The instructor of the course to add a new course");
        }

        course.setInstructor_id(user.getId());
        boolean success = courseService.addNewCourse(course);
        return success
                ? ResponseEntity.ok("Course Added Successfully")
                : ResponseEntity.internalServerError().body("Could not add the course");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCourse(@PathVariable Integer id, @RequestBody CourseRequestDTO updatedCourseDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Users users = userService.getUserByName(username);
        Optional<Courses> courseOptional =  courseService.getCourseById(id);
        Courses course = courseOptional.orElseThrow(() -> new RuntimeException("Course not found"));

        if(course.getInstructor().getId().equals(users.getId()) && users.getRole() ==  Role.INSTRUCTOR ||users.getRole() == Role.ADMIN) {

            Courses updatedCourse = courseService.getCourseByName(updatedCourseDTO.getCourseName());
            boolean success = courseService.updateCourse(id, updatedCourse);
            return success
                    ? ResponseEntity.ok("Course Updated Successfully")
                    : ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.status(403).body("Could not Update the Course");
        }



    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCourse(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Users users = userService.getUserByName(username);
        Optional<Courses> courseOptional =  courseService.getCourseById(id);
        Courses course = courseOptional.orElseThrow(() -> new RuntimeException("Course not found"));

        if(course.getInstructor().getId().equals(users.getId()) && users.getRole() ==  Role.INSTRUCTOR ||users.getRole() == Role.ADMIN) {

            boolean success = courseService.deleteCourse(id);
            return success
                    ? ResponseEntity.ok(true)
                    : ResponseEntity.status(500).body(false);
        }
        else{
            return ResponseEntity.status(403).body(false);
        }
    }


    @PostMapping("/{id}/purchase")
    @Transactional
    public ResponseEntity<String> purchaseCourse(@PathVariable Integer id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Users user = userService.getUserByName(username);


      Optional<Courses> courseOptional =  courseService.getCourseById(id);

        Courses course = courseOptional.orElseThrow(() -> new RuntimeException("Course not found"));


        if (course.getInstructor().getId().equals(user.getId())) {
            return ResponseEntity.status(400).body("You cannot purchase your own course.");
        }

          if(course.getPrice().compareTo(user.getBalance()) > 0 ){
              return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body("Not Enough Balance");
          }
          else{
              user.setBalance(user.getBalance().subtract(course.getPrice()));
              userService.updateUser(user.getId(),user);

              Enrollment enrollment =  new Enrollment(LocalDateTime.now(),course.getId(),course,user.getId(),user);
              boolean success= enrollmentService.addEnrollment(enrollment,user.getId(),course.getId());

              if(success) {
                  return ResponseEntity.ok("Course Purchased Successfully");
              }
              else{
                  return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You Can't Purchase Course more than once");
              }
          }


    }
}
