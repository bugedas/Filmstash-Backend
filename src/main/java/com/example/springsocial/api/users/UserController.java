package com.example.springsocial.api.users;

import com.example.springsocial.api.comments.Comment;
import com.example.springsocial.api.comments.CommentRepository;
import com.example.springsocial.api.exceptionHandling.exceptions.CustomDataNotFoundException;
import com.example.springsocial.api.follows.Follow;
import com.example.springsocial.api.follows.FollowRepository;
import com.example.springsocial.api.posts.Post;
import com.example.springsocial.api.posts.PostRepository;
import com.example.springsocial.api.watchingNow.WatchingNow;
import com.example.springsocial.api.watchingNow.WatchingNowRepository;
import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private PostRepository postsRepository;

    @Autowired
    private CommentRepository commentsRepository;

    @Autowired
    private WatchingNowRepository watchingNowRepository;

    @GetMapping("/api/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    @GetMapping("api/user/id/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("User of ID = " + id + " does not exist"));
    }

    @PutMapping("api/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity updateUser(@CurrentUser UserPrincipal userPrincipal, @RequestBody User user) {
        User currentUser = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        currentUser.setName(user.getName());
        currentUser.setImageUrl(user.getImageUrl());
        currentUser.setUserPrivate(user.getUserPrivate());
        final User updatedUser = userRepository.save(currentUser);

        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("api/user/{search}")
    public List<User> getUserSuggestions(@PathVariable String search) {
        List<User> allUsers = userRepository.findAll();
        List<User> filteredUsers = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getEmail().toLowerCase().contains(search.toLowerCase())
                    || user.getName().toLowerCase().contains(search.toLowerCase())) {
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }

    @GetMapping("api/user")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("api/user/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomDataNotFoundException("User of ID = " + id + " does not exist");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("api/user/metrics/{id}")
    public UserMetrics getUserMetrics(@PathVariable Long id) {

        List<Follow> allFollows = followRepository.findAll();
        int myFollowing = 0;
        int myFollowers = 0;
        for (Follow follow : allFollows) {
            if(follow.getFollowingId().equals(id)){
                myFollowing = myFollowing + 1;
            }
            if(follow.getFollowedId().equals(id)){
                myFollowers = myFollowers + 1;
            }
        }

        List<WatchingNow> allWatchingNow = watchingNowRepository.findAll();
        int myWatching = 0;
        int myWatched = 0;
        for (WatchingNow watching : allWatchingNow) {
            if(watching.getUserId().equals(id) && watching.getStillWatching()){
                myWatching = myWatching + 1;
            }
            if(watching.getUserId().equals(id) && watching.getFinished()){
                myWatched = myWatched + 1;
            }
        }

        List<Post> allPosts = postsRepository.findAll();
        int myPostsCount = 0;
        int myPostsLikeCount = 0;
        for (Post post : allPosts) {
            if(post.getUserId().equals(id)){
                myPostsCount = myPostsCount + 1;
                myPostsLikeCount = myPostsLikeCount + post.getLikes();
            }
        }

        List<Comment> allComments = commentsRepository.findAll();
        int myCommentsCount = 0;
        for (Comment comment : allComments) {
            if(comment.getUserId().equals(id)){
                myCommentsCount = myCommentsCount + 1;
            }
        }

        UserMetrics userMetrics = new UserMetrics();
        userMetrics.setFollowingCount(myFollowing);
        userMetrics.setFollowersCount(myFollowers);
        userMetrics.setWatchingTvSeriesCount(myWatching);
        userMetrics.setWatchedTvSeriesCount(myWatched);
        userMetrics.setMyPostsCount(myPostsCount);
        userMetrics.setMyPostsLikeCount(myPostsLikeCount);
        userMetrics.setMyCommentsCount(myCommentsCount);

        return userMetrics;
    }
}

class UserMetrics {
    int followingCount;
    int followersCount;
    int watchingTvSeriesCount;
    int watchedTvSeriesCount;
    int myPostsCount;
    int myPostsLikeCount;
    int myCommentsCount;

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getWatchingTvSeriesCount() {
        return watchingTvSeriesCount;
    }

    public void setWatchingTvSeriesCount(int watchingTvSeriesCount) {
        this.watchingTvSeriesCount = watchingTvSeriesCount;
    }

    public int getWatchedTvSeriesCount() {
        return watchedTvSeriesCount;
    }

    public void setWatchedTvSeriesCount(int watchedTvSeriesCount) {
        this.watchedTvSeriesCount = watchedTvSeriesCount;
    }

    public int getMyPostsCount() {
        return myPostsCount;
    }

    public void setMyPostsCount(int myPostsCount) {
        this.myPostsCount = myPostsCount;
    }

    public int getMyCommentsCount() {
        return myCommentsCount;
    }

    public void setMyCommentsCount(int myCommentsCount) {
        this.myCommentsCount = myCommentsCount;
    }

    public int getMyPostsLikeCount() {
        return myPostsLikeCount;
    }

    public void setMyPostsLikeCount(int myPostsLikeCount) {
        this.myPostsLikeCount = myPostsLikeCount;
    }
}