package com.iit.ad.entities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Key {
    private Long id;
    @NotEmpty
    private String key;
    @NotNull
    private Game game;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Key() {
    }

    public Key(Long id, @NotEmpty String key, @NotNull Game game) {
        this.id = id;
        this.key = key;
        this.game = game;
    }
}
