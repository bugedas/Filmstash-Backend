package com.example.springsocial.api.follows;

import com.example.springsocial.api.exceptionHandling.exceptions.CustomDataNotFoundException;
import com.example.springsocial.api.users.User;
import com.example.springsocial.api.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FollowsController {

    private final FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    public FollowsController(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    @GetMapping
    public List<Follow> getFollows() {
        return followRepository.findAll();
    }

    @GetMapping("/{id}")
    public Follow getFollow(@PathVariable Long id) {
        return followRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("Follows relation of ID = " + id + " does not exist"));
    }

    @GetMapping("/user/{id}")
    public List<Follow> getFollowsByUser(@PathVariable Long id) {
        List<Follow> allFollows = followRepository.findAll();
        List<Follow> followsByUser = new ArrayList<>();
        for (Follow f : allFollows
        ) {
            if (f.getFollowingId().equals(id)) {
                followsByUser.add(f);
            }
        }
        return followsByUser;
    }

    @PostMapping("/add")
    public ResponseEntity createFollow(@RequestBody Follow follow) throws URISyntaxException {
        Follow savedFollow = followRepository.save(follow);
        return ResponseEntity.created(new URI("/friends/" + savedFollow.getId())).body(savedFollow);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateFollow(@PathVariable(value = "id") Long id, @RequestBody Follow follow) {
        Follow currentFollow = followRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("Follows relation of ID = " + id + " does not exist"));
        currentFollow.setFollowingId(follow.getFollowingId());
        currentFollow.setFollowedId(follow.getFollowedId());
        final Follow updatedFollow = followRepository.save(currentFollow);

        return ResponseEntity.ok(updatedFollow);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFollow(@PathVariable Long id) {
        try {
            followRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomDataNotFoundException("Follows relation of ID = " + id + " does not exist");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/full/{id}")
    public List<User> getFollowsByUserFull(@PathVariable Long id) {
        List<Follow> allFollows = followRepository.findAll();
        List<User> usersByUser = new ArrayList<>();
        for (Follow f : allFollows
        ) {
            if (f.getFollowingId().equals(id)) {
                User user = userRepository.findById(f.getFollowedId()).orElseThrow(() -> new CustomDataNotFoundException("User of ID = " + id + " does not exist"));
                usersByUser.add(user);
            }
        }
        return usersByUser;
    }

}
