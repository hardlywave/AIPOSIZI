package com.iit.ad.service;

import com.iit.ad.exception.NoEntityException;
import com.iit.ad.model.entity.User;
import com.iit.ad.model.repository.UserRepository;
import com.iit.ad.represent.UserRepresent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> getAll() {
        Iterable<User> users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public User getById(Long id) throws NoEntityException {
        return userRepository.findById(id).orElseThrow(NoEntityException::new);
    }

    @Override
    public void create(UserRepresent userRepresent) {
        User user = new User();
        user.setEmail(userRepresent.getEmail());
        user.setUsername(userRepresent.getUsername());
        user.setPassword(userRepresent.getPassword());
        user.setDate(LocalDate.now());
        user.setSumOfAllPurchases(0);
        userRepository.save(user);
    }

    @Override
    public void update(Long id, User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String email) {
        return userRepository.findByUsername(email);
    }
}
