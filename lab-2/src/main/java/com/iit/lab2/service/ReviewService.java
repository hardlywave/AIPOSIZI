package com.iit.lab2.service;

import com.iit.lab2.persist.entity.Review;
import com.iit.lab2.persist.repo.ReviewRepository;
import com.iit.lab2.response.RestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public void create(Review review) throws RestException {
        Review newReview = new Review();
        if (reviewRepository.findByAuthor(review.getAuthor()).isPresent()) {
            throw new RestException(HttpStatus.NOT_FOUND, "Author is not exist");
        }
        if (reviewRepository.findByGame(review.getGame()).isPresent()) {
            throw new RestException(HttpStatus.NOT_FOUND, "Game is not exist");
        }
        newReview.setGame(review.getGame());
        newReview.setAuthor(review.getAuthor());
        newReview.setDate(review.getDate());
        newReview.setReview(review.getReview());
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

    public void delete(Long id) throws RestException {
        findById(id);
        reviewRepository.deleteById(id);
        log.info("The review has been removed");
    }

    public Optional<Review> findById(Long id) throws RestException {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()) {
            log.info("Review with id {} was found", id);
        } else {
            log.info("Review with id {} wasn't found", id);
            throw new RestException(HttpStatus.NOT_FOUND, "Review is not exist");
        }
        return review;
    }

    public void update(Review review) throws RestException {
        Optional<Review> row = reviewRepository.findById(review.getId());
        Review item;
        if (row.isPresent()) {
            item = row.get();
        } else {
            throw new RestException(HttpStatus.NOT_FOUND, "Review not fount");
        }
        if (!reviewRepository.findByAuthor(review.getAuthor()).isPresent()) {
            throw new RestException(HttpStatus.NOT_FOUND, "Author is not exist");
        }
        if (!reviewRepository.findByGame(review.getGame()).isPresent()) {
            throw new RestException(HttpStatus.NOT_FOUND, "Game is not exist");
        }
        if ("".equals(review.getReview())) {
            review.setReview(item.getReview());
        }
        if (Objects.isNull(review.getDate())) {
            review.setDate(item.getDate());
        }
        reviewRepository.save(review);
        log.info("The review has been updated");
    }
}
