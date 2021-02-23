package com.iit.lab2.repr;

import com.iit.lab2.persist.entity.Game;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class GameRepr {

    private Long id;
    @NotEmpty
    private String name;
    @Min(0)
    @NotNull
    private double price;
    @NotEmpty
    private String description;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetDate;

    public GameRepr() {
    }

    public GameRepr(Game game){
        id = game.getId();
        name = game.getName();
        price = game.getPrice();
        description = game.getDescription();
        targetDate = game.getDate();
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }
}
