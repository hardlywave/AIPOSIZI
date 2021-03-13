package com.iit.lab2.controllers;

import com.iit.lab2.persist.entity.User;
import com.iit.lab2.response.RestException;
import com.iit.lab2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) throws RestException {
        return userService.findById(id).get();
    }

    @PostMapping("/users/create")
    public void registerNewUser(@RequestBody User user) throws RestException {
        userService.create(user);
    }


    @DeleteMapping("/users/delete/{id}")
    public void deleteUser(@PathVariable Long id) throws RestException {
        userService.delete(id);
    }

    @PutMapping("/users/update/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody User user) throws RestException {
        user.setId(id);
        userService.update(user);
    }
}
