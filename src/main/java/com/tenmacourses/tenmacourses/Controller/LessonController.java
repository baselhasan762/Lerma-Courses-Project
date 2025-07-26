package com.tenmacourses.tenmacourses.Controller;

import com.tenmacourses.tenmacourses.DTO.CourseResponseDTO;
import com.tenmacourses.tenmacourses.DTO.LessonRequestDTO;
import com.tenmacourses.tenmacourses.DTO.LessonResponseDTO;
import com.tenmacourses.tenmacourses.Entity.Courses;
import com.tenmacourses.tenmacourses.Entity.Lessons;
import com.tenmacourses.tenmacourses.Entity.Users;
import com.tenmacourses.tenmacourses.Enums.Role;
import com.tenmacourses.tenmacourses.Service.CourseService;
import com.tenmacourses.tenmacourses.Service.LessonService;
import com.tenmacourses.tenmacourses.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/lessons")
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
    public ResponseEntity<List<LessonResponseDTO>> getAllLessons() {
        return ResponseEntity.ok(
                lessonsService.getAllLessons().stream()
                        .map(lesson -> new LessonResponseDTO(
                                lesson.getCourse().getId(),
                                lesson.getTitle(),
                                lesson.getContent_type(),
                                lesson.getContent_url(),
                                lesson.getDuration(),
                                lesson.getCreated_at()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponseDTO> getLessonById(@PathVariable int id) {
        return lessonsService.getLessonById(id)
                .map(lesson -> new LessonResponseDTO(
                        lesson.getCourse().getId(),
                        lesson.getTitle(),
                        lesson.getContent_type(),
                        lesson.getContent_url(),
                        lesson.getDuration(),
                        lesson.getCreated_at()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addLesson(@Valid @RequestPart(value = "lesson",required = true) LessonRequestDTO lessonDTO,
                                               @RequestPart(value = "file",required = true) MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.getUserByName(username);

        if(lessonDTO.getCourseId() == null){
            return ResponseEntity.badRequest().body("Course ID is Required");
        }

        Courses course = courseService.getCourseById(lessonDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course Not Found"));

        if(user.getRole() != Role.INSTRUCTOR ||
       ! course.getInstructor().getId().equals(user.getId())){
            return ResponseEntity.status(403).body("Only Instructors can add a lesson to their own courses");
        }

        Courses savedCourse = courseService.getCourseById(course.getId()).orElseThrow(()->new RuntimeException("course not found"));
            Lessons lesson = new Lessons();
            lesson.setCourse(savedCourse);
            lesson.setTitle(lessonDTO.getTitle());
            lesson.setContent_type(file.getContentType());
            lesson.setDuration(lessonDTO.getDuration());
            lesson.setCreated_at(LocalDate.now());

           boolean success = lessonsService.addLesson(lesson,file,course.getId());

            return success ?  ResponseEntity.ok("Lesson uploaded and Saved Successfully")
                    :ResponseEntity.internalServerError().body("couldn't upload the course");


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.getUserByName(username);

        Lessons lesson = lessonsService.getLessonById(id).orElseThrow(()->
        new RuntimeException("Lesson not found"));

        Optional<Courses> opcourse = courseService.getCourseById(lesson.getCourse().getId());

        if(opcourse.isEmpty()){
            return ResponseEntity.status(404).body("Course of the lesson Not Found");

        }
        Courses course = opcourse.get();


        if (!course.getInstructor().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Only instructors can Delete a lesson Of their own course");
        }



        boolean success = lessonsService.deleteLesson(id);
        return success
                ? ResponseEntity.ok("Lesson Deleted")
                : ResponseEntity.status(500).body("Lesson not found ");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateLesson(@PathVariable Integer id, @Valid @RequestPart LessonRequestDTO updatedLessonDTO, @RequestPart(required = false) MultipartFile newFile) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.getUserByName(username);

        Lessons lesson = lessonsService.getLessonById(id).orElseThrow(()-> new RuntimeException("Course not found"));

        Optional<Courses> opcourse = courseService.getCourseById(lesson.getCourse().getId());

        if(opcourse.isEmpty()){
            return ResponseEntity.status(404).body("Course of the lesson Not Found");

        }
        Courses course = opcourse.get();


        if (course == null) {
            return ResponseEntity.badRequest().body("Course ID is required");
        }

        if (user.getRole() != Role.INSTRUCTOR ||
                course.getInstructor().getId()!=(user.getId())) {
            return ResponseEntity.status(403).body("Only instructors can update a lesson of their own course");
        }

         Lessons updatedLesson=lessonsService.getLessonByName(updatedLessonDTO);
        boolean success = lessonsService.updateLesson(id, updatedLesson,newFile);
        return success
                ? ResponseEntity.ok("Lesson Updated Successfully")
                : ResponseEntity.notFound().build();
    }



};