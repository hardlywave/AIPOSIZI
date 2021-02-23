package com.iit.lab2.persist.repo;

import com.iit.lab2.persist.entity.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GameRepository extends CrudRepository<Game, Long> {
    Optional<Game> findByName(String name);
}
