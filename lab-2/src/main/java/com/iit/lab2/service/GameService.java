package com.iit.lab2.service;

import com.iit.lab2.persist.entity.Game;
import com.iit.lab2.persist.repo.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }


    public void create(Game game) {
        gameRepository.save(game);
    }

    public List<Game> findAll() {
        Iterable<Game> source = gameRepository.findAll();
        List<Game> target = new ArrayList<>();
        source.forEach(target::add);
        return target;
    }

    public Optional<Game> findByName(String name) {
        return gameRepository.findByName(name);
    }

    public void delete(Long id) {
        gameRepository.deleteById(id);
    }

    public Optional<Game> findById(Long id) {
        return gameRepository.findById(id);
    }

    public void update(Game game) {
        gameRepository.save(game);
    }
}
