package com.iit.lab2.controllers;

import com.iit.lab2.repr.UserRepr;
import com.iit.lab2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserRepr());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute("user") @Valid UserRepr userRepr,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        if (!userRepr.getPassword().equals(userRepr.getMatchingPassword())) {
            result.rejectValue("password", "", "Password not matching");
            return "register";
        }
        if(userService.findByEmail(userRepr.getEmail()).isPresent()){
            result.rejectValue("email", "", "Email is exist");
            return "register";
        }
        if(userService.findByUsername(userRepr.getUsername()).isPresent()){
            result.rejectValue("username", "", "Username is exist");
            return "register";
        }
        userService.create(userRepr);
        return "redirect:/users";
    }
}
