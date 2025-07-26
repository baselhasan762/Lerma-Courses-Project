package com.tenmacourses.tenmacourses.Controller;

import com.tenmacourses.tenmacourses.DTO.ReviewRequestDTO;
import com.tenmacourses.tenmacourses.DTO.ReviewResponseDTO;
import com.tenmacourses.tenmacourses.Entity.Courses;
import com.tenmacourses.tenmacourses.Entity.Reviews;
import com.tenmacourses.tenmacourses.Entity.Users;
import com.tenmacourses.tenmacourses.Enums.Role;
import com.tenmacourses.tenmacourses.Service.CourseService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewsService reviewsService;
    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public ReviewController(ReviewsService reviewsService,UserService userService, CourseService courseService) {
        this.reviewsService = reviewsService;
        this.userService = userService;
        this.courseService= courseService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews() {
        List<ReviewResponseDTO> reviews = reviewsService.getAllReviews().stream()
                .map(review -> {
                    ReviewResponseDTO dto = new ReviewResponseDTO();
                    dto.setUserId(review.getUser().getId());
                    dto.setCourseId(review.getCourse().getId());
                    dto.setRating(review.getRating());
                    dto.setComment(review.getComment());
                    dto.setReviewText(review.getReviewText());
                    dto.setCreatedAt(review.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());;
        return ResponseEntity.ok(reviews);
    }



    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable int id) {
        return reviewsService.getReviewById(id)
                .map(review -> {
                    ReviewResponseDTO dto = new ReviewResponseDTO();
                    dto.setUserId(review.getUser().getId());
                    dto.setCourseId(review.getCourse().getId());
                    dto.setRating(review.getRating());
                    dto.setComment(review.getComment());
                    dto.setReviewText(review.getReviewText());
                    dto.setCreatedAt(review.getCreatedAt());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@RequestBody ReviewRequestDTO UpdatedReview, @PathVariable int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.getUserByName(authentication.getName());

            Optional<Reviews> opreview = reviewsService.getReviewById(id);
            if(opreview.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review ID not found");
            }

            Reviews review = opreview.get();

            if(!review.getUser().getId().equals(user.getId() )){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot Update A review That is not Yours");
            };

           boolean success = reviewsService.updateReview(id,UpdatedReview);
           return success ? ResponseEntity.ok("Review Updated Successfully") :
                   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Couldn't update review");

    }

    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody ReviewRequestDTO review) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.getUserByName(authentication.getName());

        Optional<Courses> opcourse =courseService.getCourseById(review.getCourseId());
        if(opcourse.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");

        }

        Courses course=opcourse.get();

        if (course.getInstructor().getId()==user.getId()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You cannot delete a review from your course!");
        }




        if( course.getInstructor().getId() == user.getId() ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot Add A review to your own course !");
        }

        boolean success = reviewsService.addReview(review);
        return success ? ResponseEntity.ok("Review Added Successfully") : ResponseEntity.internalServerError().body("Couldn't Add Review");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.getUserByName(authentication.getName());

        Optional<Reviews> optionalReview = reviewsService.getReviewById(id);

        if (optionalReview.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        }

        Reviews review = optionalReview.get();

        Optional<Courses> opcourse =courseService.getCourseById(review.getCourse().getId());
        if(opcourse.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");

        }

        Courses course=opcourse.get();

        if (course.getInstructor().getId()==user.getId()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You cannot delete a review from your course!");
        }


        boolean success = reviewsService.deleteReview(id);
        return success
                ? ResponseEntity.ok("Review Deleted Successfully")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Couldn't Delete Review");
    }


}
