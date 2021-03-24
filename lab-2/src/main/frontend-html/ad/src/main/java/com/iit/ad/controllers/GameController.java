package com.iit.ad.controllers;

import com.google.gson.Gson;
import com.iit.ad.entities.Game;
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
public class GameController {
    private final Service service;
    private final String url = "http://localhost:8082/games";
    private final Gson gson;

    @Autowired
    public GameController(Service service, Gson gson) {
        this.service = service;
        this.gson = gson;
    }

    @GetMapping("/games")
    public String getGames(Model model) throws URISyntaxException, EntityException {
        ResponseEntity<Request> response = service.getMethod(url);
        List<Game> gameList = (List<Game>) Objects.requireNonNull(response.getBody()).getData();
        model.addAttribute("games", gameList);
        return "games";
    }

    @GetMapping("/games/delete/{id}")
    public String deleteGame(@PathVariable Long id) {
        service.deleteMethod(url + "/delete/" + id);
        return "redirect:/games";
    }

    @GetMapping("/games/update/{id}")
    public String updateGame(@PathVariable Long id, Model model) throws EntityException {
        ResponseEntity<Request> response = service.getMethod(url + "/update/" + id);
        if (Objects.nonNull(Objects.requireNonNull(response.getBody()).getData())) {
            String json = new Gson().toJson(Objects.requireNonNull(response.getBody()).getData());
            Game game = gson.fromJson(json, Game.class);
            model.addAttribute("game", game);
            return "updateGame";
        } else {
            return "games";
        }
    }

    @PostMapping("/games/update/{id}")
    public String updateGame(@PathVariable Long id, @ModelAttribute("game") @Valid Game game,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "updateGame";
        }
        try {
            service.postMethod(url + "/update/" + id, game);
        } catch (EntityException e) {
            result.rejectValue(e.getType(), "", e.getMessage());
            return "updateGame";
        }
        return "redirect:/games";
    }

    @GetMapping("/games/create")
    public String registerPage(Model model) {
        model.addAttribute("game", new Game());
        return "createGame";
    }

    @PostMapping("/games/create")
    public String registerNewGame(@ModelAttribute("game") @Valid Game game,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "createGame";
        }
        try {
            service.postMethod(url + "/create", game);
        } catch (EntityException e) {
            result.rejectValue(e.getType(), "", e.getMessage());
            return "createGame";
        }
        return "redirect:/games";
    }
}
