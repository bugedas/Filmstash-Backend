package com.example.springsocial.api.follows;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "friend")
public class Follow {
    @Id
    @GeneratedValue
    private Long id;
    private Long followingId;
    private Long followedId;

    public Follow(Long followingId, Long followedId) {
        this.followingId = followingId;
        this.followedId = followedId;
    }

    public Follow() {
        this.followingId = null;
        this.followedId = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }

    public Long getFollowedId() {
        return followedId;
    }

    public void setFollowedId(Long followedId) {
        this.followedId = followedId;
    }
}
