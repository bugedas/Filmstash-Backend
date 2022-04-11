package com.example.springsocial.api.posts;

import com.example.springsocial.api.exceptionHandling.exceptions.CustomDataNotFoundException;
import com.example.springsocial.model.User;
import com.example.springsocial.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/posts")
public class PostsController {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    public PostsController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository; this.userRepository = userRepository;
    }

    @GetMapping
    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return postRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("Post of ID = " + id + " does not exist"));
    }

    @GetMapping("/user/{id}")
    public List<Post> getPostsByUserId(@PathVariable Long id) {
        List<Post> allPosts = postRepository.findAll();
        List<Post> postsByUser = new ArrayList<>();
        for (Post p : allPosts
        ) {
            if (p.getUserId().equals(id)) {
                postsByUser.add(p);
            }
        }
        return postsByUser;
    }

    @PostMapping("/add")
    public ResponseEntity createPost(@RequestBody Post post) throws URISyntaxException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        post.setDate(timestamp);
        Post savedPost = postRepository.save(post);
        return ResponseEntity.created(new URI("/posts/" + savedPost.getId())).body(savedPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePost(@PathVariable(value = "id") Long id, @RequestBody Post post) {
        Post currentPost = postRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("Post of ID = " + id + " does not exist"));
        currentPost.setUserId(post.getUserId());
        currentPost.setFilmId(post.getFilmId());
        currentPost.setMessage(post.getMessage());
        currentPost.setLikes(post.getLikes());
        currentPost.setDate(post.getDate());
        currentPost.setType(post.getType());
        final Post updatedPost = postRepository.save(currentPost);

        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePost(@PathVariable Long id) {
        try {
            postRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomDataNotFoundException("Post of ID = " + id + " does not exist");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/film/{id}/{type}")
    public List<PostUser> getPostsByFilmId(@PathVariable String id, @PathVariable String type ) {
        List<Post> allPosts = postRepository.findAll();
        List<PostUser> postsByFilm = new ArrayList<>();
        for (Post p : allPosts
        ) {
            if (p.getFilmId().equals(id) && p.getType().equals(type)) {
                PostUser postUser = new PostUser();
                User user = userRepository.getById(p.getUserId());
                postUser.setPost(p);
                postUser.setUserName(user.getName());
                postsByFilm.add(postUser);
            }
        }
        return postsByFilm;
    }
}

class PostUser {
    public Post post;

    public String userName;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}