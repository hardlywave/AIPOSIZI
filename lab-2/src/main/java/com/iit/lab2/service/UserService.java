package com.iit.lab2.service;

import com.iit.lab2.persist.entity.Review;
import com.iit.lab2.persist.entity.User;
import com.iit.lab2.persist.repo.ReviewRepository;
import com.iit.lab2.persist.repo.UserRepository;
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
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public UserService(UserRepository userRepository, ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    public void create(User userRequest) throws RestException {
        User user = new User();
        user.copyAttribute(userRequest);
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Username is busy");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Email is busy");
        }
        userRepository.save(user);
        log.info("{} was created", user.getUsername());
    }

    public List<User> findAll() {
        Iterable<User> source = userRepository.findAll();
        List<User> target = new ArrayList<>();
        source.forEach(target::add);
        log.info("{} addresses was found", target.size());
        return target;
    }

    public void delete(Long id) throws RestException {
        User user = findById(id).get();
        reviewRepository.deleteByAuthor(user);
        userRepository.deleteById(id);
        log.info("The user has been removed");
    }

    public Optional<User> findById(Long id) throws RestException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            log.info("User with id {} was found", id);
        } else {
            log.info("User with id {} wasn't found", id);
            throw new RestException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

    public void update(User user) throws RestException {
        User oldUser = userRepository.findById(user.getId()).get();
        if (!user.getUsername().equals(oldUser.getUsername())) {
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Username is busy");
            }
        }
        if (!user.getEmail().equals(oldUser.getEmail())) {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Email is busy");
            }
        }
        setNotNull(user, oldUser);
        userRepository.save(user);
        log.info("{} has been updated", user.getUsername());
    }

    private void setNotNull(User userOne, User userTwo) {
        if ("".equals(userOne.getUsername())) {
            userOne.setUsername(userTwo.getUsername());
        }
        if ("".equals(userOne.getEmail())) {
            userOne.setEmail(userTwo.getEmail());
        }
        if ("".equals(userOne.getPassword())) {
            userOne.setPassword(userTwo.getPassword());
        }
        if (Objects.isNull(userOne.getDate())) {
            userOne.setDate(userTwo.getDate());
        }
    }
}
