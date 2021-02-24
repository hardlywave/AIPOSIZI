package com.iit.lab2.repr;

import com.iit.lab2.persist.entity.Review;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ReviewRepr {
    private Long id;
    @NotEmpty
    private String author;
    @NotEmpty
    private String game;
    @NotEmpty
    private String review;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public ReviewRepr() {
    }

    public ReviewRepr(Review review){
        id = review.getId();
        author = review.getAuthor().getUsername();
        game = review.getGame().getName();
        this.review = review.getReview();
        date = review.getDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
