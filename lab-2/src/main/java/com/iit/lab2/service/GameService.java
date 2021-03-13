package com.iit.lab2.service;

import com.iit.lab2.persist.entity.Game;
import com.iit.lab2.persist.repo.GameRepository;
import com.iit.lab2.persist.repo.KeyRepository;
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
public class GameService {

    private final GameRepository gameRepository;
    private final KeyRepository keyRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public GameService(GameRepository gameRepository, KeyRepository keyRepository, ReviewRepository reviewRepository) {
        this.gameRepository = gameRepository;
        this.keyRepository = keyRepository;
        this.reviewRepository = reviewRepository;
    }


    public void create(Game game) throws RestException {
        Game game1 = new Game();
        game1.copyAttribute(game);
        if (gameRepository.findByName(game1.getName()).isPresent()) {
            throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Name is busy");
        }
        gameRepository.save(game1);
        log.info("{} was created", game.getName());
    }

    public List<Game> findAll() {
        Iterable<Game> source = gameRepository.findAll();
        List<Game> target = new ArrayList<>();
        source.forEach(target::add);
        log.info("{} games was found", target.size());
        return target;
    }

    public void delete(Long id) throws RestException {
        Game game = findById(id).get();
        keyRepository.deleteByGame(game);
        reviewRepository.deleteByGame(game);
        gameRepository.deleteById(id);
        log.info("The game has been removed");
    }

    public Optional<Game> findById(Long id) throws RestException {
        Optional<Game> game = gameRepository.findById(id);
        if (game.isPresent()) {
            log.info("Game with id {} was found", id);
        } else {
            log.info("Game with id {} wasn't found", id);
            throw new RestException(HttpStatus.NOT_FOUND, "Game not found");
        }
        return game;
    }

    public void update(Game game) throws RestException {
        Optional<Game> row = findById(game.getId());
        Game item;
        if (row.isPresent()) {
            item = row.get();
            if (!item.getName().equals(game.getName())) {
                if (gameRepository.findByName(game.getName()).isPresent()) {
                    throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Name is busy");
                }
            }
        } else {
            throw new RestException(HttpStatus.NOT_FOUND, "Not found!");
        }
        if (Objects.isNull(game.getDate())) {
            game.setDate(item.getDate());
        }
        if ("".equals(game.getDescription())) {
            game.setDescription(item.getDescription());
        }
        if (Objects.isNull(game.getPrice())) {
            game.setPrice(item.getPrice());
        }
        gameRepository.save(game);
        log.info("The game has been updated");
    }
}
