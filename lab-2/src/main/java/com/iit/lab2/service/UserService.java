package com.iit.lab2.service;

import com.iit.lab2.persist.entity.Game;
import com.iit.lab2.persist.entity.Review;
import com.iit.lab2.persist.entity.User;
import com.iit.lab2.persist.repo.UserRepository;
import com.iit.lab2.repr.UserRepr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(UserRepr userRepr) {
        User user = new User();
        user.setEmail(userRepr.getEmail());
        user.setUsername(userRepr.getUsername());
        user.setPassword(userRepr.getPassword());
        user.setDate(LocalDate.now());
        userRepository.save(user);
        log.info("{} was created", userRepr.getUsername());
    }

    public List<User> findAll() {
        Iterable<User> source = userRepository.findAll();
        List<User> target = new ArrayList<>();
        source.forEach(target::add);
        log.info("{} addresses was found", target.size());
        return target;
    }

    public Optional<User> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            log.info("Game with email {} was found", email);
        }else {
            log.info("Game with email {} wasn't found", email);
        }
        return user;
    }

    public Optional<User> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            log.info("Game with username {} was found", username);
        }else {
            log.info("Game with username {} wasn't found", username);
        }
        return user;
    }

    public void delete(Long id){
        userRepository.deleteById(id);
        log.info("The user has been removed");
    }

    public Optional<User> findById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            log.info("User with id {} was found", id);
        }else {
            log.info("User with id {} wasn't found", id);
        }
        return user;
    }

    public void update(User user){
        User oldUser = userRepository.findById(user.getId()).get();
        if("".equals(user.getPassword())){
            user.setPassword(oldUser.getPassword());
        }
        user.setDate(oldUser.getDate());
        userRepository.save(user);
        log.info("{} has been updated", user.getUsername());
    }
}
