package com.iit.ad.controllers;

import com.google.gson.Gson;
import com.iit.ad.entities.Key;
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
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@Controller
public class KeyController {
    private final Service service;
    private final String url = "http://localhost:8082/keys";
    private final Gson gson;

    @Autowired
    public KeyController(Service service, Gson gson) {
        this.service = service;
        this.gson = gson;
    }

    @GetMapping("/keys")
    public String getKeys(Model model) throws URISyntaxException, EntityException {
        ResponseEntity<Request> response = service.getMethod(url);
        List<Key> keyList = (List<Key>) Objects.requireNonNull(response.getBody()).getData();
        model.addAttribute("keys", keyList);
        return "keys";
    }

    @GetMapping("/keys/delete/{id}")
    public String deleteKey(@PathVariable Long id) {
        service.deleteMethod(url + "/delete/" + id);
        return "redirect:/keys";
    }

    @GetMapping("/keys/update/{id}")
    public String updateKey(@PathVariable Long id, Model model) throws EntityException {
        ResponseEntity<Request> response = service.getMethod(url + "/update/" + id);
        if (Objects.nonNull(Objects.requireNonNull(response.getBody()).getData())) {
            String json = gson.toJson(Objects.requireNonNull(response.getBody()).getData());
            Key key = gson.fromJson(json, Key.class);
            model.addAttribute("key", key);
            return "updateKey";
        } else {
            return "keys";
        }
    }

    @PostMapping("/keys/update/{id}")
    public String updateKey(@PathVariable Long id, @ModelAttribute("key") @Valid Key key,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "updateKey";
        }
        try {
            String gamesUrl = "http://localhost:8082/games/find";
            service.postMethod(gamesUrl, key.getGame());
            service.postMethod(url + "/update/" + id, key);
        } catch (EntityException e) {
            result.rejectValue(e.getType(), "", e.getMessage());
            return "updateKey";
        }
        return "redirect:/keys";
    }

    @GetMapping("/keys/create")
    public String registerPage(Model model) {
        model.addAttribute("key", new Key());
        return "createKey";
    }

    @PostMapping("/keys/create")
    public String registerNewKey(@ModelAttribute("key") @Valid Key key,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "createKey";
        }
        try {
            String gamesUrl = "http://localhost:8082/games/find";
            service.postMethod(gamesUrl, key.getGame());
            service.postMethod(url + "/create", key);
        } catch (EntityException e) {
            result.rejectValue(e.getType(), "", e.getMessage());
            return "createKey";
        }
        return "redirect:/keys";
    }
}
