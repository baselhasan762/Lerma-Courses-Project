package com.tenmacourses.tenmacourses.Controller;

import com.tenmacourses.tenmacourses.DTO.LessonDTO;
import com.tenmacourses.tenmacourses.Entity.Courses;
import com.tenmacourses.tenmacourses.Entity.Lessons;
import com.tenmacourses.tenmacourses.Entity.Users;
import com.tenmacourses.tenmacourses.Enums.Role;
import com.tenmacourses.tenmacourses.Service.CourseService;
import com.tenmacourses.tenmacourses.Service.LessonService;
import com.tenmacourses.tenmacourses.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonsService;
    private final UserService userService;
    private final CourseService courseService;


    @Autowired
    public LessonController(LessonService lessonsService, UserService userService,CourseService courseService) {
        this.lessonsService = lessonsService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Lessons>> getAllLessons() {
        return ResponseEntity.ok(lessonsService.getAllLessons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lessons> getLessonById(@PathVariable int id) {
        return lessonsService.getLessonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/uploadLesson", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addLesson(@RequestPart("lesson") LessonDTO lessonDTO,
                                               @RequestPart("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.getUserByName(username);

        if(lessonDTO.getCourseId() == null){
            return ResponseEntity.badRequest().body("Course ID is Required");
        }

        Courses course = courseService.getCourseById(lessonDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course Not Found"));

        if(user.getRole() != Role.INSTRUCTOR ||
        !course.getInstructor().getId().equals(user.getId())){
            return ResponseEntity.status(403).body("Only Instructors can add a lesson to their own courses");
        }


            Lessons lesson = new Lessons();
            lesson.setCourse(course);
            lesson.setTitle(lessonDTO.getTitle());
            lesson.setContent_type(file.getContentType());
            lesson.setDuration(lessonDTO.getDuration());
            lesson.setCreated_at(LocalDateTime.now());

           boolean success = lessonsService.addLesson(lesson,file,course.getId());

            return success ?  ResponseEntity.ok("Lesson uploaded and Saved Successfully")
                    :ResponseEntity.internalServerError().body("couldn't upload the course");


    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.getUserByName(username);

        Lessons lesson = lessonsService.getLessonById(id).orElseThrow(()->
        new RuntimeException("Course not found"));


        if (user.getRole() != Role.INSTRUCTOR ||
                !lesson.getCourse().getInstructor().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Only instructors can Delete a lesson Of their own course");
        }



        boolean success = lessonsService.deleteLesson(id);
        return success
                ? ResponseEntity.ok("Lesson Deleted")
                : ResponseEntity.status(500).body("Lesson not found ");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateLesson(@PathVariable Integer id, @RequestBody Lessons updatedLesson) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.getUserByName(username);

        Lessons lesson = lessonsService.getLessonById(id).orElseThrow(()-> new RuntimeException("Course not found"));
        Integer CourseId =lesson.getCourse().getId();


        if (lesson.getCourse() == null || CourseId  == null) {
            return ResponseEntity.badRequest().body("Course ID is required");
        }

        Courses course = courseService.getCourseById(lesson.getCourse().getId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (user.getRole() != Role.INSTRUCTOR ||
                !course.getInstructor().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Only instructors can update a lesson of their own course");
        }


        boolean success = lessonsService.updateLesson(id, updatedLesson);
        return success
                ? ResponseEntity.ok("Lesson Updated Successfully")
                : ResponseEntity.notFound().build();
    }



};