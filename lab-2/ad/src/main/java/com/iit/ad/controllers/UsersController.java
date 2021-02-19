package com.iit.ad.controllers;

import com.iit.ad.exception.NoEntityException;
import com.iit.ad.model.entity.User;
import com.iit.ad.represent.UserRepresent;
import com.iit.ad.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class UsersController {
    private static final String REDIRECT_USERS = "redirect:/users";
    private final UserServiceImpl userService;

    @Autowired
    public UsersController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String createNewUser(Model model) {
        model.addAttribute("newUser", new UserRepresent());
        return "registration";
    }

    @GetMapping("/users")
    public String getAll(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/users/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        try {
            User user = userService.getById(id);
            model.addAttribute("user", user);
        } catch (NoEntityException noEntityException) {
            log.warn("User not found!");
            return "redirect:404";
        }
        return "user";
    }

    @GetMapping("users/{id}/update")
    public String updateUser(@PathVariable Long id, Model model) {
        try {
            User user = userService.getById(id);
            model.addAttribute("user", user);
        } catch (NoEntityException noEntityException) {
            log.warn("User not found!");
        }
        return "registration";
    }

    @GetMapping("/users/{id}/delete}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return REDIRECT_USERS;
    }

    @PostMapping("/registration")
    public String registerNewUser(@Valid UserRepresent userRepresent) {
        userService.create(userRepresent);
        return "redirect:users";
    }
}
