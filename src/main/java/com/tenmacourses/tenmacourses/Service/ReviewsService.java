package com.tenmacourses.tenmacourses.Service;

import com.tenmacourses.tenmacourses.DTO.ReviewRequestDTO;
import com.tenmacourses.tenmacourses.DTO.ReviewResponseDTO;
import com.tenmacourses.tenmacourses.Entity.Reviews;
import com.tenmacourses.tenmacourses.Repository.CourseRepo;
import com.tenmacourses.tenmacourses.Repository.ReviewRepo;
import com.tenmacourses.tenmacourses.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewsService {

    private final ReviewRepo reviewsRepo;
    private final UserRepo userRepo;
    private final CourseRepo courseRepo;

    @Autowired
    public ReviewsService(ReviewRepo reviewsRepo,UserRepo userRepo,CourseRepo courseRepo) {
        this.reviewsRepo = reviewsRepo;
        this.userRepo=userRepo;
        this.courseRepo=courseRepo;
    }

    public List<Reviews> getAllReviews() {
        return reviewsRepo.findAll();
    }


    public Optional<Reviews> getReviewById(int id) {
        return reviewsRepo.findById(id);
    }

    public boolean addReview(ReviewRequestDTO dto) {
        try {
            Reviews review = new Reviews();
            review.setUser(userRepo.findById(dto.getUserId()).orElseThrow());
            review.setCourse(courseRepo.findById(dto.getCourseId()).orElseThrow());
            review.setRating(dto.getRating());
            review.setComment(dto.getComment());
            review.setReviewText(dto.getReviewText());
            review.setCreatedAt(LocalDate.now());

            reviewsRepo.save(review);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public boolean deleteReview(int id) {
        try {
            reviewsRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateReview(int id, ReviewRequestDTO updatedReview) {
        return reviewsRepo.findById(id).map(existingReview -> {
            existingReview.setRating(updatedReview.getRating());
            existingReview.setReviewText(updatedReview.getReviewText());
            existingReview.setCreatedAt(LocalDate.now());

            reviewsRepo.save(existingReview);
            return true;
        }).orElse(false);
    }

}
