package com.iit.ad.service;

import com.iit.ad.exception.NoEntityException;
import com.iit.ad.model.entity.User;
import com.iit.ad.represent.UserRepresent;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();

    User getById(Long id) throws NoEntityException;

    void create(UserRepresent userRepresent) throws NoEntityException;

    void update(Long id, User user);

    void delete(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String email);
}
