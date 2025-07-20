package com.tenmacourses.tenmacourses.Service;

import com.tenmacourses.tenmacourses.Entity.Enrollment;
import com.tenmacourses.tenmacourses.Repository.EnrollmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    private final EnrollmentRepo enrollmentRepo;

    @Autowired
    public EnrollmentService(EnrollmentRepo enrollmentRepo) {
        this.enrollmentRepo = enrollmentRepo;
    }

    public List<Enrollment> getAll() {
        return enrollmentRepo.findAll();
    }

    public Optional<Enrollment> getById(int id) {
        return enrollmentRepo.findById(id);
    }

    public boolean addEnrollment(Enrollment enrollment) {
        try {
            enrollmentRepo.save(enrollment);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteEnrollment(int id) {
        try {
            enrollmentRepo.deleteById(id);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
