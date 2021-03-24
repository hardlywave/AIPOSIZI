package com.iit.ad.controllers;

import com.google.gson.Gson;
import com.iit.ad.entities.Review;
import com.iit.ad.respnose.EntityException;
import com.iit.ad.respnose.Request;
import com.iit.ad.service.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@Controller
public class ReviewController {
    private final Service service;
    private final String url = "http://localhost:8082/reviews";
    private final Gson gson;

    public ReviewController(Service service, Gson gson) {
        this.service = service;
        this.gson = gson;
    }


    @GetMapping("/reviews")
    public String getReviews(Model model) throws URISyntaxException, EntityException {
        ResponseEntity<Request> response = service.getMethod(url);
        List<Review> reviewList = (List<Review>) Objects.requireNonNull(response.getBody()).getData();
        model.addAttribute("reviews", reviewList);
        return "reviews";
    }

    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        service.deleteMethod(url + "/delete/" + id);
        return "redirect:/reviews";
    }

    @GetMapping("/reviews/update/{id}")
    public String updateReview(@PathVariable Long id, Model model) throws EntityException {
        ResponseEntity<Request> response = service.getMethod(url + "/update/" + id);
        if (Objects.nonNull(Objects.requireNonNull(response.getBody()).getData())) {
            String json = new Gson().toJson(Objects.requireNonNull(response.getBody()).getData());
            Review review = gson.fromJson(json, Review.class);
            model.addAttribute("review", review);
            return "updateReview";
        } else {
            return "reviews";
        }
    }

    @PostMapping("/reviews/update/{id}")
    public String updateReview(@PathVariable Long id, @ModelAttribute("review") @Valid Review review,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "updateReview";
        }
        try {
            service.postMethod(url + "/update/" + id, review);
        } catch (EntityException e) {
            result.rejectValue(e.getType(), "", e.getMessage());
            return "updateReview";
        }
        return "redirect:/reviews";
    }

    @GetMapping("/reviews/create")
    public String registerPage(Model model) {
        model.addAttribute("review", new Review());
        return "createReview";
    }

    @PostMapping("/reviews/create")
    public String registerNewReview(@ModelAttribute("review") @Valid Review review,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "createReview";
        }
        try {
            service.postMethod(url + "/create", review);
        } catch (EntityException e) {
            result.rejectValue(e.getType(), "", e.getMessage());
            return "createReview";
        }
        return "redirect:/reviews";
    }
}
