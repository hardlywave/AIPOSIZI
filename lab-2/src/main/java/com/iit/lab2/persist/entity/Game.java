package com.iit.lab2.persist.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column (nullable = false)
    private Double price;
    @Column (nullable = false)
    private String description;
    @Column (nullable = false)
    private LocalDate date;

    public Game() {
    }

    public void copyAttribute(Game game){
        this.name = game.getName();
        this.price = game.getPrice();
        this.description = game.getDescription();
        this.date = game.getDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) && Objects.equals(name, game.name) && Objects.equals(price, game.price) && Objects.equals(description, game.description) && Objects.equals(date, game.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, description, date);
    }
}
