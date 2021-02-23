package com.iit.lab2.persist.repo;

import com.iit.lab2.persist.entity.Key;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface KeyRepository extends CrudRepository<Key, Long> {
    Optional<Key> findByKey(String key);
}
