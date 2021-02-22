package com.iit.lab2.persist.entity;

import javax.persistence.*;

@Entity
@Table(name = "keys")
public class Key {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String key;
    @Column(nullable = false)
    private String Game;
    public Key() {
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
        return Game;
    }

    public void setGame(String game) {
        Game = game;
    }
}
