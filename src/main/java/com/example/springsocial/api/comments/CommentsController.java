package com.example.springsocial.api.comments;

import com.example.springsocial.api.exceptionHandling.exceptions.CustomDataNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/comments")
public class CommentsController {

    private final CommentRepository commentRepository;

    public CommentsController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @GetMapping
    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Comment getComment(@PathVariable Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("Comment of ID = " + id + " does not exist"));
    }

    @GetMapping("/post/{id}")
    public List<Comment> getCommentsByPost(@PathVariable Long id) {
        List<Comment> allComments = commentRepository.findAll();
        List<Comment> commentsByUser = new ArrayList<>();
        for (Comment f : allComments
        ) {
            if (f.getPostId().equals(id)) {
                commentsByUser.add(f);
            }
        }
        return commentsByUser;
    }

    @PostMapping("/add")
    public ResponseEntity createComment(@RequestBody Comment comment) throws URISyntaxException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        comment.setDate(timestamp);
        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.created(new URI("/comments/" + savedComment.getId())).body(savedComment);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateComment(@PathVariable(value = "id") Long id, @RequestBody Comment comment) {
        Comment currentComment = commentRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("Comment of ID = " + id + " does not exist"));
        currentComment.setPostId(comment.getPostId());
        currentComment.setUserId(comment.getUserId());
        currentComment.setMessage(comment.getMessage());
        currentComment.setLikes(comment.getLikes());
        currentComment.setDate(comment.getDate());
        final Comment updatedComment = commentRepository.save(currentComment);

        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteComment(@PathVariable Long id) {
        try {
            commentRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomDataNotFoundException("Comment of ID = " + id + " does not exist");
        }
        return ResponseEntity.ok().build();
    }

}
