package com.iit.lab2.persist.repo;

import com.iit.lab2.persist.entity.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Long> {
}
