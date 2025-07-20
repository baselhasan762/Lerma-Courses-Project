package com.tenmacourses.tenmacourses.Controller;

import com.tenmacourses.tenmacourses.Entity.Reviews;
import com.tenmacourses.tenmacourses.Service.ReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewsService reviewsService;

    @Autowired
    public ReviewController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    @GetMapping
    public ResponseEntity<List<Reviews>> getAllReviews() {
        List<Reviews> reviews = reviewsService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reviews> getReviewById(@PathVariable int id) {
        return reviewsService.getReviewById(id)
                .map(reviews -> ResponseEntity.ok(reviews))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Boolean> addReview(@RequestBody Reviews review) {
        boolean success = reviewsService.addReview(review);
        return success ? ResponseEntity.ok(true) : ResponseEntity.internalServerError().body(false);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteReview(@PathVariable int id) {
        boolean success = reviewsService.deleteReview(id);
        return success ? ResponseEntity.ok(true) : ResponseEntity.status(500).body(false);
    }
}
