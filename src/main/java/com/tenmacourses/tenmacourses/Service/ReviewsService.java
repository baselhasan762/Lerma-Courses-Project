package com.tenmacourses.tenmacourses.Service;

import com.tenmacourses.tenmacourses.Entity.Reviews;
import com.tenmacourses.tenmacourses.Repository.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewsService {

    private final ReviewRepo reviewsRepo;

    @Autowired
    public ReviewsService(ReviewRepo reviewsRepo) {
        this.reviewsRepo = reviewsRepo;
    }

    public List<Reviews> getAllReviews() {
        return reviewsRepo.findAll();
    }

    public Optional<Reviews> getReviewById(int id) {
        return reviewsRepo.findById(id);
    }

    public boolean addReview(Reviews review) {
        try {
            reviewsRepo.save(review);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
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

    public boolean updateReview(int id, Reviews updatedReview) {
        return reviewsRepo.findById(id).map(existingReview -> {
            existingReview.setRating(updatedReview.getRating());
            existingReview.setReviewText(updatedReview.getReviewText());
            existingReview.setCreatedAt(updatedReview.getCreatedAt());

            reviewsRepo.save(existingReview);
            return true;
        }).orElse(false);
    }

}
