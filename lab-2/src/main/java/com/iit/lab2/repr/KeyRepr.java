package com.iit.lab2.repr;

import com.iit.lab2.persist.entity.Key;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

public class KeyRepr {
    private Long id;
    @NotEmpty
    private String key;
    @NotEmpty
    private String game;

    public KeyRepr() {
    }

    public KeyRepr(Key key){
        id = key.getId();
        this.key = key.getKey();
        game = key.getGame().getName();
    }

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

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }
}
