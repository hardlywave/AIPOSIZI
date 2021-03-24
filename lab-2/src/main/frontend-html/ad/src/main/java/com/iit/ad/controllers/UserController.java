package com.iit.ad.controllers;

import com.google.gson.Gson;
import com.iit.ad.entities.User;
import com.iit.ad.respnose.EntityException;
import com.iit.ad.respnose.Request;
import com.iit.ad.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {
    private final Service service;
    private final String url = "http://localhost:8082/users";
    private final Gson gson;

    @Autowired
    public UserController(Service service, Gson gson) {
        this.service = service;
        this.gson = gson;
    }

    @GetMapping("/users")
    public String getUsers(Model model) throws EntityException {
        ResponseEntity<Request> response = service.getMethod(url);
        List<User> userList = (List<User>) Objects.requireNonNull(response.getBody()).getData();
        model.addAttribute("users", userList);
        return "users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        service.deleteMethod(url + "/delete/" + id);
        return "redirect:/users";
    }

    @GetMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id, Model model) throws EntityException {
        ResponseEntity<Request> response = service.getMethod(url + "/update/" + id);
        if (Objects.nonNull(Objects.requireNonNull(response.getBody()).getData())) {
            String json = gson.toJson(Objects.requireNonNull(response.getBody()).getData());
            User user = gson.fromJson(json, User.class);
            model.addAttribute("user", user);
            return "update";
        } else {
            return "users";
        }
    }

    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") @Valid User user,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "update";
        }
        try {
            service.postMethod(url + "/update/" + id, user);
        } catch (EntityException e) {
            result.rejectValue(e.getType(), "", e.getMessage());
            return "update";
        }
        return "redirect:/users";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute("user") @Valid User user,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            service.postMethod(url + "/create", user);
        } catch (EntityException e) {
            result.rejectValue(e.getType(), "", e.getMessage());
            return "register";
        }
        return "redirect:/users";
    }
}
