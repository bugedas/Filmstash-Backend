package com.example.springsocial.api.likes;

import com.example.springsocial.api.exceptionHandling.exceptions.CustomDataNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(path = "/api/likes")
public class LikesController {

    private final LikeRepository likeRepository;

    public LikesController(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @GetMapping
    public List<Like> getLikes() {
        return likeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Like getLike(@PathVariable String id) {
        return likeRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("Like of ID = " + id + " does not exist"));
    }

    @PostMapping("/add")
    public ResponseEntity createLike(@RequestBody Like like) throws URISyntaxException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        like.setDate(timestamp);
        Like savedLike = likeRepository.save(like);
        return ResponseEntity.created(new URI("/likes/" + savedLike.getId())).body(savedLike);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLike(@PathVariable String id) {
        try {
            likeRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomDataNotFoundException("Like of ID = " + id + " does not exist");
        }
        return ResponseEntity.ok().build();
    }

}
