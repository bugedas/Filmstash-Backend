package com.example.springsocial.api.likes;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "likes")
public class Like {
    @Id
    private String id;
    private String filmId;
    private Long userId;
    private Timestamp date;


    public Like(String id, String filmId, Long userId) {
        this.id = id;
        this.filmId = filmId;
        this.userId = userId;
    }

    public Like() {
        this.filmId = "";
        this.userId = null;
        this.date = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}