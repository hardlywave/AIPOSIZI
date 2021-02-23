package com.iit.lab2.controllers;

import com.iit.lab2.persist.entity.Key;
import com.iit.lab2.service.KeyService;
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
public class KeyController {

    private final KeyService keyService;

    @Autowired
    public KeyController(KeyService keyService) {
        this.keyService = keyService;
    }

    @GetMapping("/keys")
    public String getKeys(Model model) {
        List<Key> keys = keyService.findAll();
        model.addAttribute("keys", keys);
        return "keys";
    }

    @GetMapping("/keys/delete/{id}")
    public String deleteKey(@PathVariable Long id) {
        keyService.delete(id);
        return "redirect:keys";
    }

    @GetMapping("/keys/update/{id}")
    public String updateKey(@PathVariable Long id, Model model) {
        Optional<Key> key = keyService.findById(id);
        if (key.isPresent()) {
            model.addAttribute("key", key.get());
            return "updateKey";
        } else {
            return "keys";
        }
    }

    @PostMapping("/keys/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("key") @Valid Key key,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "updateKey";
        }
        keyService.update(key);
        return "redirect:/keys";
    }

    @GetMapping("/keys/create")
    public String registerPage(Model model) {
        model.addAttribute("key", new Key());
        return "createKey";
    }

    @PostMapping("/keys/create")
    public String createNewKey(@ModelAttribute("key") @Valid Key key,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "createKey";
        }
        keyService.update(key);
        return "redirect:/keys";
    }
}