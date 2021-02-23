package com.iit.lab2.service;

import com.iit.lab2.persist.entity.Review;
import com.iit.lab2.persist.repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void create(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> findAll() {
        Iterable<Review> source = reviewRepository.findAll();
        List<Review> target = new ArrayList<>();
        source.forEach(target::add);
        return target;
    }

    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public void update(Review review) {
        reviewRepository.save(review);
    }
}
