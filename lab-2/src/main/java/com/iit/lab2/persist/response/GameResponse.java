package com.iit.lab2.persist.response;

import com.iit.lab2.persist.entity.Game;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GameResponse {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private LocalDate date;
    private byte[] linkMainImage;
    private List<byte[]> linksImages;

    public GameResponse(Game game) {
        id = game.getId();
        name = game.getName();
        price = game.getPrice();
        description = game.getDescription();
        date = game.getDate();
        linksImages = new ArrayList<>();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setLinkMainImage(byte[] linkMainImage) {
        this.linkMainImage = linkMainImage;
    }

    public void setLinksImages(List<byte[]> linksImages) {
        this.linksImages = linksImages;
    }

    public void addScreenshot(byte[] screenshot) {
        linksImages.add(screenshot);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public byte[] getLinkMainImage() {
        return linkMainImage;
    }

    public List<byte[]> getLinksImages() {
        return linksImages;
    }
}
