package com.iit.lab2.service;

import com.iit.lab2.bucket.BucketName;
import com.iit.lab2.filestore.FileStore;
import com.iit.lab2.persist.entity.Game;
import com.iit.lab2.persist.repo.GameRepository;
import com.iit.lab2.persist.repo.KeyRepository;
import com.iit.lab2.persist.repo.ReviewRepository;
import com.iit.lab2.response.RestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final KeyRepository keyRepository;
    private final ReviewRepository reviewRepository;
    private final FileStore fileStore;

    @Autowired
    public GameService(GameRepository gameRepository, KeyRepository keyRepository, ReviewRepository reviewRepository, FileStore fileStore) {
        this.gameRepository = gameRepository;
        this.keyRepository = keyRepository;
        this.reviewRepository = reviewRepository;
        this.fileStore = fileStore;
    }


    public void create(Game game) throws RestException {
        Game game1 = new Game();
        game1.copyAttribute(game);
        if (gameRepository.findByName(game1.getName()).isPresent()) {
            throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Name is busy", "name");
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
            log.info("Image with id {} was found", id);
        } else {
            log.info("Image with id {} wasn't found", id);
            throw new RestException(HttpStatus.NOT_FOUND, "Image not found", "game");
        }
        return game;
    }

    public Optional<Game> findByName(String name) throws RestException {
        Optional<Game> game = gameRepository.findByName(name);
        if (game.isPresent()) {
            log.info("Image with name {} was found", name);
        } else {
            log.info("Image with name {} wasn't found", name);
            throw new RestException(HttpStatus.NOT_FOUND, "Image not found", "game");
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
                    throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Name is busy", "name");
                }
            }
        } else {
            throw new RestException(HttpStatus.NOT_FOUND, "Not found!", "game");
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

    public void uploadMainImage(Long id, MultipartFile file) {
//        Check if image is not empty
        isFileEmpty(file);
//        If file is an image
        isImage(file);
//        The game exist in our db
        Game game = gameRepository.findById(id).orElseThrow(() -> new IllegalStateException("Image not found"));
//        Gram some metadata from file if any
        Map<String, String> metadata = extractMetadata(file);
//        Store the image in s3 and update db(link) with s3 image lik
        String path = String.format("%s/%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), "games", game.getId());
        String fileName = String.format("%s-%s", file.getName(), UUID.randomUUID());
        try {
            fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
            game.setLinkMainImage(fileName);
            gameRepository.save(game);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void uploadScreenshot(Long id, MultipartFile file) {
//        Check if image is not empty
        isFileEmpty(file);
//        If file is an image
        isImage(file);
//        The game exist in our db
        Game game = gameRepository.findById(id).orElseThrow(() -> new IllegalStateException("Image not found"));
//        Gram some metadata from file if any
        Map<String, String> metadata = extractMetadata(file);
//        Store the image in s3 and update db(link) with s3 image lik
        String path = String.format("%s/%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), "games", game.getId());
        String fileName = String.format("%s-%s", file.getName(), UUID.randomUUID());
        try {
            fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
            game.addLinkImage(fileName);
            gameRepository.save(game);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    byte[] downloadGameScreenshot(Game game, int numberScreenshot) {
        String path = String.format("%s/%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), "games", game.getId());

        return Optional.of(game.getLinksImages().get(numberScreenshot))
                .map(key -> fileStore.download(path, key))
                .orElse(new byte[0]);

    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(ContentType.IMAGE_JPEG.getMimeType(), ContentType.IMAGE_PNG.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file[" + file.getSize() + "]");
        }
    }

    public byte[] downloadMainImage(Long id) throws RestException {
        Game game = findById(id).get();
        System.out.println(id);
        String path = String.format("%s/%s/%s",
                BucketName.PROFILE_IMAGE.getBucketName(),
                "games",
                game.getId());
        return game.getLinkMainImage().map(key -> fileStore.download(path, key))
                .orElse(new byte[0]);
    }
}
