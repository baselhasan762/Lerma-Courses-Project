package com.tenmacourses.tenmacourses.Controller;

import com.tenmacourses.tenmacourses.DTO.EnrollmentDTO;
import com.tenmacourses.tenmacourses.Entity.Enrollment;
import com.tenmacourses.tenmacourses.Service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        List<EnrollmentDTO> enrollments = enrollmentService.getAll().stream()
                .map(enrollDTO -> new EnrollmentDTO(
                        enrollDTO.getEnrolledAt(),
                        enrollDTO.getCourse().getId(),
                        enrollDTO.getUser().getId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(enrollments);
    }


    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable int id) {
        return enrollmentService.getById(id)
                .map(enrollment -> {
                    EnrollmentDTO dto = new EnrollmentDTO(
                            enrollment.getEnrolledAt(),
                            enrollment.getCourse().getId(),
                            enrollment.getUser().getId()
                    );
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEnrollment(@PathVariable int id) {
        boolean success = enrollmentService.deleteEnrollment(id);
        return success
                ? ResponseEntity.ok(true)
                : ResponseEntity.status(500).body(false);
    }
}
