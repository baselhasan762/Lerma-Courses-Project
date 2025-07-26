package com.tenmacourses.tenmacourses.Service;

import com.tenmacourses.tenmacourses.DTO.CourseResponseDTO;
import com.tenmacourses.tenmacourses.DTO.LessonRequestDTO;
import com.tenmacourses.tenmacourses.DTO.LessonResponseDTO;
import com.tenmacourses.tenmacourses.Entity.Courses;
import com.tenmacourses.tenmacourses.Entity.Lessons;
import com.tenmacourses.tenmacourses.Repository.CourseRepo;
import com.tenmacourses.tenmacourses.Repository.LessonsRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LessonService {

    private final LessonsRepo lessonsRepo;
    private final CourseRepo courseRepo;

    @Autowired
    public LessonService(LessonsRepo lessonsRepo,CourseRepo courseRepo) {
        this.courseRepo = courseRepo;
        this.lessonsRepo = lessonsRepo;
    }

    public List<Lessons> getAllLessons() {
        return lessonsRepo.findAll();
    }


    public Optional<Lessons> getLessonById(int id) {
        return lessonsRepo.findById(id);
    }


    public boolean addLesson(Lessons lesson, MultipartFile file, Integer courseId) {
        try {
            String uploadDir = "uploads/courses/"+courseId;
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            String filename = UUID.randomUUID()+"_"+file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(),filePath, StandardCopyOption.REPLACE_EXISTING);
            lesson.setContent_url(uploadDir + "/" + filename);

            lessonsRepo.save(lesson);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteLesson(int id) {
      return lessonsRepo.findById(id).map(lesson->{
          String filePath = lesson.getContent_url();
          File file = new File(filePath);
          if(file.exists()){
              file.delete();
          }
          lessonsRepo.deleteById(id);
          return true;
      }).orElse(false);


    }

    public boolean updateLesson(Integer id, Lessons updatedLesson, MultipartFile newFile) {
        return lessonsRepo.findById(id).map(existingLesson -> {
            try {
                if (newFile != null && !newFile.isEmpty()) {
                    String oldFilePath = existingLesson.getContent_url();
                    File oldFile = new File(oldFilePath);
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }

                    String uploadDir = "uploads/courses/" + existingLesson.getCourse().getId();
                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    String newFilename = UUID.randomUUID() + "_" + newFile.getOriginalFilename();
                    Path filePath = uploadPath.resolve(newFilename);
                    Files.copy(newFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    existingLesson.setContent_url(filePath.toString());
                }

                existingLesson.setTitle(updatedLesson.getTitle());
                existingLesson.setContent_type(updatedLesson.getContent_type());
                existingLesson.setCreated_at(LocalDate.now());

                if (updatedLesson.getCourse().getId() != null) {
                    Courses course = courseRepo.findById(updatedLesson.getCourse().getId()).orElseThrow(() -> new RuntimeException("Course not found with ID: " + updatedLesson.getCourse().getId()));
                    existingLesson.setCourse(course);
                }


                lessonsRepo.save(existingLesson);
                return true;

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }).orElse(false);
    }


    public Lessons getLessonByName(@Valid LessonRequestDTO updatedLessonDTO) {
      return  lessonsRepo.findByTitle(updatedLessonDTO.getTitle());
    }
}
