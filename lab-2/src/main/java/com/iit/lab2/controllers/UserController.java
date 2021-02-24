package com.iit.lab2.controllers;

import com.iit.lab2.persist.entity.User;
import com.iit.lab2.repr.UserRepr;
import com.iit.lab2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getUsers(Model model){
        List<User> userList = userService.findAll();
        model.addAttribute("users", userList);
        return "users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:users";
    }

    @GetMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id, Model model){
        Optional<User> user = userService.findById(id);
        if(user.isPresent()){
            model.addAttribute("user", user.get());
            return "update";
        }else {
            return "users";
        }
    }

    @PostMapping("/users/update/{id}")
    public String registerNewUser(@PathVariable Long id, @ModelAttribute("user") @Valid User user,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "update";
        }
        User oldUser = userService.findById(user.getId()).get();
        if(!oldUser.getEmail().equals(user.getEmail())){
            if (userService.findByEmail(user.getEmail()).isPresent()) {
                result.rejectValue("email", "", "Email is exist");
                return "update";
            }
        }
        if(!oldUser.getUsername().equals(user.getUsername())){
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                result.rejectValue("username", "", "Username is exist");
                return "update";
            }
        }
        userService.update(user);
        return "redirect:/users";
    }
}
