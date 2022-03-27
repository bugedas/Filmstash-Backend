package com.example.springsocial.api.watchingNow;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "watching_now")
public class WatchingNow {
    @Id
    @GeneratedValue
    private Long id;
    private String tvId;
    private Long userId;
    private int seasonNr;
    private int episodeNr;
    private boolean finished;
    private boolean stillWatching;

    public WatchingNow(Long id, String tvId, Long userId, int seasonNr, int episodeNr, boolean finished, boolean stillWatching) {
        this.id = id;
        this.tvId = tvId;
        this.userId = userId;
        this.seasonNr = seasonNr;
        this.episodeNr = episodeNr;
        this.stillWatching = stillWatching;
    }

    public WatchingNow() {
        this.id = null;
        this.tvId = "";
        this.userId = null;
        this.seasonNr = 0;
        this.episodeNr = 0;
        this.finished = false;
        stillWatching = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTvId() {
        return tvId;
    }

    public void setTvId(String tvId) {
        this.tvId = tvId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getSeasonNr() {
        return seasonNr;
    }

    public void setSeasonNr(int seasonNr) {
        this.seasonNr = seasonNr;
    }

    public int getEpisodeNr() {
        return episodeNr;
    }

    public void setEpisodeNr(int episodeNr) {
        this.episodeNr = episodeNr;
    }

    public boolean getFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean getStillWatching() {
        return stillWatching;
    }

    public void setStillWatching(boolean stillWatching) {
        this.stillWatching = stillWatching;
    }
}
