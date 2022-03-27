package com.example.springsocial.api.films;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "film")
public class Film {
    @Id
    @GeneratedValue
    private Long id;
    private String filmId;
    private Long userId;
    private int userRated;
    private String listType;
    private String type;

    public Film(Long id, String filmId, Long userId, int userRated, String listType, String type) {
        this.id = id;
        this.filmId = filmId;
        this.userId = userId;
        this.userRated = userRated;
        this.listType = listType;
        this.type = type;
    }

    public Film() {
        this.id = null;
        this.filmId = "";
        this.userId = null;
        this.userRated = 0;
        this.listType = "";
        this.type = "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public int getUserRated() {
        return userRated;
    }

    public void setUserRated(int userRated) {
        this.userRated = userRated;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
