package com.tenmacourses.tenmacourses.Controller;

import com.tenmacourses.tenmacourses.Entity.Enrollment;
import com.tenmacourses.tenmacourses.Service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable int id) {
        return enrollmentService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Boolean> addEnrollment( @Valid @RequestBody Enrollment enrollment) {
        boolean success = enrollmentService.addEnrollment(enrollment);
        return success
                ? ResponseEntity.ok(true)
                : ResponseEntity.internalServerError().body(false);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEnrollment(@PathVariable int id) {
        boolean success = enrollmentService.deleteEnrollment(id);
        return success
                ? ResponseEntity.ok(true)
                : ResponseEntity.status(500).body(false);
    }
}
