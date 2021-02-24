package com.iit.lab2.controllers;

import com.iit.lab2.EntityNotFoundException;
import com.iit.lab2.persist.entity.Review;
import com.iit.lab2.repr.ReviewRepr;
import com.iit.lab2.service.GameService;
import com.iit.lab2.service.ReviewService;
import com.iit.lab2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
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
        List<ReviewRepr> reviewReprs = new ArrayList<>();
        for (Review review : reviews) {
            reviewReprs.add(new ReviewRepr(review));
        }
        model.addAttribute("reviews", reviewReprs);
        return "reviews";
    }

    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return "redirect:/reviews";
    }

    @GetMapping("/reviews/update/{id}")
    public String updateReview(@PathVariable Long id, Model model) {
        Optional<Review> review = reviewService.findById(id);
        if (review.isPresent()) {
            model.addAttribute("review", new ReviewRepr(review.get()));
            return "updateReview";
        } else {
            return "reviews";
        }
    }

    @PostMapping("/reviews/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("review") @Valid ReviewRepr review,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "updateReview";
        }
        try {
            reviewService.update(review);
            return "redirect:/reviews";
        }catch (EntityNotFoundException e){
            result.rejectValue(e.getType(), "", e.getMessage());
            return "updateReview";
        }
    }

    @GetMapping("/reviews/create")
    public String registerPage(Model model) {
        model.addAttribute("review", new ReviewRepr());
        return "createReview";
    }

    @PostMapping("/reviews/create")
    public String createNewReview(@ModelAttribute("review") @Valid ReviewRepr review,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "createReview";
        }
        try {
            reviewService.create(review);
            return "redirect:/reviews";
        }catch (EntityNotFoundException e){
            result.rejectValue(e.getType(), "", e.getMessage());
            return "createReview";
        }
    }
}
