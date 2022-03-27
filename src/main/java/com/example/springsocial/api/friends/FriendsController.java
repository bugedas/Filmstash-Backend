package com.example.springsocial.api.friends;

import com.example.springsocial.api.exceptionHandling.exceptions.CustomDataNotFoundException;
import com.example.springsocial.controller.UserController;
import com.example.springsocial.model.User;
import com.example.springsocial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendsController {

    private final FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    public FriendsController(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    @GetMapping
    public List<Friend> getFriends() {
        return friendRepository.findAll();
    }

    @GetMapping("/{id}")
    public Friend getFriend(@PathVariable Long id) {
        return friendRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("Friends relation of ID = " + id + " does not exist"));
    }

    @GetMapping("/user/{id}")
    public List<Friend> getFriendsByUser(@PathVariable Long id) {
        List<Friend> allFriends = friendRepository.findAll();
        List<Friend> friendsByUser = new ArrayList<>();
        for (Friend f : allFriends
        ) {
            if (f.getFollowingId().equals(id)) {
                friendsByUser.add(f);
            }
        }
        return friendsByUser;
    }

    @PostMapping("/add")
    public ResponseEntity createFriend(@RequestBody Friend friend) throws URISyntaxException {
        Friend savedFriend = friendRepository.save(friend);
        return ResponseEntity.created(new URI("/friends/" + savedFriend.getId())).body(savedFriend);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateFriend(@PathVariable(value = "id") Long id, @RequestBody Friend friend) {
        Friend currentFriend = friendRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("Friends relation of ID = " + id + " does not exist"));
        currentFriend.setFollowingId(friend.getFollowingId());
        currentFriend.setFollowedId(friend.getFollowedId());
        final Friend updatedFriend = friendRepository.save(currentFriend);

        return ResponseEntity.ok(updatedFriend);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFriend(@PathVariable Long id) {
        try {
            friendRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomDataNotFoundException("Friends relation of ID = " + id + " does not exist");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/full/{id}")
    public List<User> getFriendsByUserFull(@PathVariable Long id) {
        List<Friend> allFriends = friendRepository.findAll();
        List<User> usersByUser = new ArrayList<>();
        for (Friend f : allFriends
        ) {
            if (f.getFollowingId().equals(id)) {
                User user = userRepository.findById(f.getFollowedId()).orElseThrow(() -> new CustomDataNotFoundException("User of ID = " + id + " does not exist"));
                usersByUser.add(user);
            }
        }
        return usersByUser;
    }

}
