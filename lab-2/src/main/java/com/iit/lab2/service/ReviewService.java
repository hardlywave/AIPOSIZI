package com.iit.lab2.service;

import com.iit.lab2.persist.entity.Game;
import com.iit.lab2.persist.entity.Review;
import com.iit.lab2.persist.repo.ReviewRepository;
import com.iit.lab2.repr.ReviewRepr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void create(ReviewRepr review) {
        Review newReview = new Review();
        newReview.setAuthor(review.getAuthor());
        newReview.setReview(review.getReview());
        newReview.setGame(review.getGame());
        newReview.setDate(review.getDate());
        reviewRepository.save(newReview);
        log.info("Review was created");
    }

    public List<Review> findAll() {
        Iterable<Review> source = reviewRepository.findAll();
        List<Review> target = new ArrayList<>();
        source.forEach(target::add);
        log.info("{} addresses was found", target.size());
        return target;
    }

    public void delete(Long id) {
        reviewRepository.deleteById(id);
        log.info("The review has been removed");
    }

    public Optional<Review> findById(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        if(review.isPresent()){
            log.info("Review with id {} was found", id);
        }else {
            log.info("Review with id {} wasn't found", id);
        }
        return review;
    }

    public void update(ReviewRepr review) {
        Review newReview = new Review();
        newReview.setId(review.getId());
        newReview.setReview(review.getReview());
        newReview.setDate(review.getDate());
        newReview.setGame(review.getGame());
        newReview.setAuthor(review.getAuthor());
        reviewRepository.save(newReview);
        log.info("The review has been updated");
    }
}
