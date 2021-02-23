package com.iit.lab2.controllers;

import com.iit.lab2.persist.entity.Review;
import com.iit.lab2.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews")
    public String getReviews(Model model) {
        List<Review> reviews = reviewService.findAll();
        model.addAttribute("reviews", reviews);
        return "reviews";
    }

    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return "redirect:reviews";
    }

    @GetMapping("/reviews/update/{id}")
    public String updateReview(@PathVariable Long id, Model model) {
        Optional<Review> review = reviewService.findById(id);
        if (review.isPresent()) {
            model.addAttribute("review", review.get());
            return "updateReview";
        } else {
            return "reviews";
        }
    }

    @PostMapping("/reviews/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("review") @Valid Review review,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "updateReview";
        }
        reviewService.update(review);
        return "redirect:/reviews";
    }

    @GetMapping("/reviews/create")
    public String registerPage(Model model) {
        model.addAttribute("review", new Review());
        return "createReview";
    }

    @PostMapping("/reviews/create")
    public String createNewReview(@ModelAttribute("review") @Valid Review review,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "createReview";
        }
        reviewService.create(review);
        return "redirect:/reviews";
    }
}
