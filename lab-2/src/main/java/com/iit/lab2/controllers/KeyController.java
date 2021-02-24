package com.iit.lab2.controllers;

import com.iit.lab2.EntityNotFoundException;
import com.iit.lab2.persist.entity.Key;
import com.iit.lab2.persist.entity.Review;
import com.iit.lab2.repr.KeyRepr;
import com.iit.lab2.repr.ReviewRepr;
import com.iit.lab2.service.GameService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class KeyController {

    private final KeyService keyService;
    private final GameService gameService;

    @Autowired
    public KeyController(KeyService keyService, GameService gameService) {
        this.keyService = keyService;
        this.gameService = gameService;
    }

    @GetMapping("/keys")
    public String getKeys(Model model) {
        List<Key> keys = keyService.findAll();
        List<KeyRepr> keyReprs = new ArrayList<>();
        for (Key key : keys) {
            keyReprs.add(new KeyRepr(key));
        }
        model.addAttribute("keys", keyReprs);
        return "keys";
    }

    @GetMapping("/keys/delete/{id}")
    public String deleteKey(@PathVariable Long id) {
        keyService.delete(id);
        return "redirect:/keys";
    }

    @GetMapping("/keys/update/{id}")
    public String updateKey(@PathVariable Long id, Model model) {
        Optional<Key> key = keyService.findById(id);
        if (key.isPresent()) {
            model.addAttribute("key", new KeyRepr(key.get()));
            return "updateKey";
        } else {
            return "keys";
        }
    }

    @PostMapping("/keys/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("key") @Valid KeyRepr key,
                         BindingResult result) throws EntityNotFoundException {
        if (result.hasErrors()) {
            return "updateKey";
        }
        try {
            keyService.update(key);
            return "redirect:/keys";
        }catch (EntityNotFoundException e){
            result.rejectValue(e.getType(), "", e.getMessage());
            return "updateKey";
        }
    }

    @GetMapping("/keys/create")
    public String registerPage(Model model) {
        model.addAttribute("key", new KeyRepr());
        return "createKey";
    }

    @PostMapping("/keys/create")
    public String createNewKey(@ModelAttribute("key") @Valid KeyRepr key,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "createKey";
        }
        try {
            keyService.create(key);
            return "redirect:/keys";
        }catch (EntityNotFoundException e){
            result.rejectValue(e.getType(), "", e.getMessage());
            return "createKey";
        }
    }
}
