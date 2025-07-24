package com.tenmacourses.tenmacourses.Controller;

import com.tenmacourses.tenmacourses.Entity.Reviews;
import com.tenmacourses.tenmacourses.Entity.Users;
import com.tenmacourses.tenmacourses.Enums.Role;
import com.tenmacourses.tenmacourses.Service.ReviewsService;
import com.tenmacourses.tenmacourses.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewsService reviewsService;
    private final UserService userService;

    @Autowired
    public ReviewController(ReviewsService reviewsService,UserService userService) {
        this.reviewsService = reviewsService;
        this.userService = userService;
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

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReview(@RequestBody Reviews UpdatedReview,@PathVariable int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.getUserByName(authentication.getName());

            Optional<Reviews> opreview = reviewsService.getReviewById(id);
            if(opreview.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review ID not found");
            }

            Reviews review = opreview.get();

            if(review.getUser().getId() != user.getId() ){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot Update A review That is not Yours");
            };

           boolean success = reviewsService.updateReview(id,UpdatedReview);
           return success ? ResponseEntity.ok("Review Updated Successfully") :
                   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Couldn't update review");

    }

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody Reviews review) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.getUserByName(authentication.getName());

        if(user.getRole().equals(Role.INSTRUCTOR) && review.getCourse().getInstructor().getId() != user.getId() ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot Add A review to your own course !");
        }

        boolean success = reviewsService.addReview(review);
        return success ? ResponseEntity.ok("Review Added Successfully") : ResponseEntity.internalServerError().body("Couldn't Add Review");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.getUserByName(authentication.getName());

        Optional<Reviews> optionalReview = reviewsService.getReviewById(id);

        if (optionalReview.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        }

        Reviews review = optionalReview.get();

        if (user.getRole().equals(Role.INSTRUCTOR) &&
                !review.getCourse().getInstructor().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You cannot delete a review from another instructor's course!");
        }

        boolean success = reviewsService.deleteReview(id);
        return success
                ? ResponseEntity.ok("Lesson Deleted Successfully")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Couldn't Delete Lesson");
    }


}
