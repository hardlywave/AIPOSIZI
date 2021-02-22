package com.iit.lab2.service;

import com.iit.lab2.persist.entity.User;
import com.iit.lab2.persist.repo.UserRepository;
import com.iit.lab2.repr.UserRepr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    }

    public List<User> findAll() {
        Iterable<User> source = userRepository.findAll();
        List<User> target = new ArrayList<>();
        source.forEach(target::add);
        return target;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void delete(Long id){
        userRepository.deleteById(id);
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public void update(User user){
        User oldUser = userRepository.findById(user.getId()).get();
        if("".equals(user.getPassword())){
            user.setPassword(oldUser.getPassword());
        }
        user.setDate(oldUser.getDate());
        userRepository.save(user);
    }
}
