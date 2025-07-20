package com.tenmacourses.tenmacourses.Controller;

import com.tenmacourses.tenmacourses.DTO.UserResponseDTO;
import com.tenmacourses.tenmacourses.Entity.Courses;
import com.tenmacourses.tenmacourses.Entity.UserPrincipal;
import com.tenmacourses.tenmacourses.Entity.Users;
import com.tenmacourses.tenmacourses.Enums.Role;
import com.tenmacourses.tenmacourses.Service.CourseService;
import com.tenmacourses.tenmacourses.Service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Courses>> getAllCourses() {
        List<Courses> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Courses> getCourseById(@PathVariable Integer id) {
        return courseService.getCourseById(id)
                .map(course -> ResponseEntity.ok(course))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> addNewCourse(@RequestBody Courses course) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.getUserByName(username);

        if(user.getRole() != Role.INSTRUCTOR || user.getId() != course.getInstructor().getId()){
            return ResponseEntity.status(403).body("you must be The instructor of the course to add a new course");
        }

        course.setInstructor(user);
        boolean success = courseService.addNewCourse(course);
        return success
                ? ResponseEntity.ok("Course Added Successfully")
                : ResponseEntity.internalServerError().body("Could not add the course");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCourse(@PathVariable Integer id, @RequestBody Courses updatedCourse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Users users = userService.getUserByName(username);
        Optional<Courses> courseOptional =  courseService.getCourseById(id);
        Courses course = courseOptional.orElseThrow(() -> new RuntimeException("Course not found"));

        if(course.getInstructor().getId().equals(users.getId()) && users.getRole() ==  Role.INSTRUCTOR ||users.getRole() == Role.ADMIN) {

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


    @PostMapping("/{id}")
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
              return  ResponseEntity.ok("Course Purchased Successfully");
          }


    }
}
