package com.iit.ad.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Game {
    private Long id;
    @NotEmpty
    private String name;
    @Min(0)
    @NotNull
    private Double price;
    @NotEmpty
    private String description;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String linkMainImage;
    private List<String> linksImages;

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

    public String getLinkMainImage() {
        return linkMainImage;
    }

    public void setLinkMainImage(String linkMainImage) {
        this.linkMainImage = linkMainImage;
    }

    public List<String> getLinksImages() {
        return linksImages;
    }

    public void setLinksImages(List<String> linksImages) {
        this.linksImages = linksImages;
    }

    public Game() {
    }

    public Game(Long id, @NotEmpty String name, @Min(0) @NotNull Double price, @NotEmpty String description, @NotNull LocalDate date, String linkMainImage, List<String> linksImages) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.date = date;
        this.linkMainImage = linkMainImage;
        this.linksImages = linksImages;
    }
}
