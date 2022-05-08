package com.example.springsocial.api;

import com.example.springsocial.api.comments.Comment;
import com.example.springsocial.api.films.Film;
import com.example.springsocial.api.follows.Follow;
import com.example.springsocial.api.likes.Like;
import com.example.springsocial.api.posts.Post;
import com.example.springsocial.api.users.User;
import com.example.springsocial.api.watchingNow.WatchingNow;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Mocks {

    public static Comment CommentMock() {
        Comment com = new Comment();
        com.setMessage("TEST TEXT");
        com.setPostId(1L);
        com.setUserId(1L);
        return com;
    }

    public static Comment CommentMock(String message) {
        Comment com = new Comment();
        com.setMessage(message);
        com.setPostId(1L);
        com.setUserId(1L);
        return com;
    }

    public static Comment CommentMock(String message, Long postId) {
        Comment com = new Comment();
        com.setMessage(message);
        com.setPostId(postId);
        com.setUserId(1L);
        return com;
    }

    public static Comment CommentMock(String message, Long postId, Long userId) {
        Comment com = CommentMock(message, postId);
        com.setUserId(1L);
        return com;
    }

    public static Film FilmMock() {
        Film film = new Film();
        film.setFilmId("TESTID");
        film.setListType("TEST_TYPE");
        film.setUserId(0L);
        film.setType("watched");
        return film;
    }

    public static Film FilmMock(String listType) {
        Film film = FilmMock();
        film.setListType(listType);
        return film;
    }

    public static Film FilmMock(String listType, String type) {
        Film film = FilmMock(listType);
        film.setType(type);
        return film;
    }

    public static Film FilmMock(String listType, String type, Long userId) {
        Film film = FilmMock(listType, type);
        film.setUserId(userId);
        return film;
    }

    public static Film FilmMock(String listType, String type, Long userId, String filmId) {
        Film film = FilmMock(listType, type, userId);
        film.setFilmId(filmId);
        return film;
    }

    public static Film FilmMock(String listType, String type, Long userId, String filmId, Long id) {
        Film film = FilmMock(listType, type, userId, filmId);
        film.setId(id);
        return film;
    }

    public static Follow FollowMock(Long id, Long followingId, Long followedId) {
        Follow follow = new Follow();
        follow.setId(id);
        follow.setFollowedId(followedId);
        follow.setFollowingId(followingId);
        return follow;
    }

    public static User UserMock(Long id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    public static Like LikeMock(String id, String filmId, Long userId) {
        Like like = new Like();
        like.setId(id);
        like.setFilmId(filmId);
        like.setUserId(userId);
        return like;
    }

    public static Post PostMock(Long id, String filmId, Long userId, String message) {
        Post post = new Post();
        post.setId(id);
        post.setFilmId(filmId);
        post.setUserId(userId);
        post.setMessage(message);
        return post;
    }

    public static Post PostMock(Long id, String filmId, Long userId, String message, String type) {
        Post post = PostMock(id, filmId, userId, message);
        post.setType(type);
        return post;
    }

    public static Post PostMock(Long id, String filmId, Long userId, String message, String type, int likes) {
        Post post = PostMock(id, filmId, userId, message, type);
        post.setLikes(likes);
        return post;
    }

    public static WatchingNow WatchingNowMock(Long id, String tvId, Long userId, boolean stillWatching, boolean finished, int seasonNr, int episodeNr) {
        WatchingNow watchingNow = new WatchingNow();
        watchingNow.setId(id);
        watchingNow.setTvId(tvId);
        watchingNow.setUserId(userId);
        watchingNow.setStillWatching(stillWatching);
        watchingNow.setFinished(finished);
        watchingNow.setSeasonNr(seasonNr);
        watchingNow.setEpisodeNr(episodeNr);
        return watchingNow;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
