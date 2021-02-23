package com.iit.lab2.service;

import com.iit.lab2.persist.entity.Game;
import com.iit.lab2.persist.repo.GameRepository;
import com.iit.lab2.repr.GameRepr;
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
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }


    public void create(GameRepr game) {
        Game newGame = new Game();
        newGame.setDate(game.getTargetDate());
        newGame.setDescription(game.getDescription());
        newGame.setName(game.getName());
        newGame.setPrice(game.getPrice());
        gameRepository.save(newGame);
        log.info("{} was created", newGame.getName());

    }

    public List<Game> findAll() {
        Iterable<Game> source = gameRepository.findAll();
        List<Game> target = new ArrayList<>();
        source.forEach(target::add);
        log.info("{} addresses was found", target.size());
        return target;
    }

    public Optional<Game> findByName(String name) {
        Optional<Game> game = gameRepository.findByName(name);
        if(game.isPresent()){
            log.info("A game named {} was found", name);
        }else {
            log.info("A game named {} wasn't found", name);
        }
        return game;
    }

    public void delete(Long id) {
        gameRepository.deleteById(id);
        log.info("The game has been removed");
    }

    public Optional<Game> findById(Long id) {
        Optional<Game> game = gameRepository.findById(id);
        if(game.isPresent()){
            log.info("Game with id {} was found", id);
        }else {
            log.info("Game with id {} wasn't found", id);
        }
        return game;
    }

    public void update(Game game) {
        gameRepository.save(game);
        log.info("The game has been updated");
    }
}
