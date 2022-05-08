package com.example.springsocial.api.comments;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.example.springsocial.api.Mocks.CommentMock;
import static com.example.springsocial.api.Mocks.asJsonString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentRepository repository;

    @WithMockUser
    @Test
    public void createCommentTest() throws Exception {
        Comment comment1 = CommentMock();
        doReturn(comment1).when(repository).save(Mockito.any());
        mockMvc.perform(post("/api/comments/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(comment1)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @WithMockUser
    @Test
    public void findAllTest() throws Exception {
        Comment comment1 = CommentMock();
        Comment comment2 = CommentMock("TEST 2");
        doReturn(Lists.newArrayList(comment1, comment2)).when(repository).findAll();
        mockMvc.perform(get("/api/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @WithMockUser
    @Test
    public void findByIdTest() throws Exception {
        Comment comment1 = CommentMock();
        doReturn(Optional.of(comment1)).when(repository).findById(0L);
        mockMvc.perform(get("/api/comments/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("TEST TEXT")));
    }

    @WithMockUser
    @Test
    public void findByPostIdTest() throws Exception {
        Comment comment1 = CommentMock("TEST MESSAGE", 0L);
        Comment comment2 = CommentMock("TEST MESSAGE 2", 1L);
        doReturn(Lists.newArrayList(comment1, comment2)).when(repository).findAll();
        mockMvc.perform(get("/api/comments/post/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message", is("TEST MESSAGE")));
    }

    @WithMockUser
    @Test
    public void updateCommentTest() throws Exception {
        Comment comment1 = CommentMock("TEST MESSAGE", 0L);
        Comment comment2 = CommentMock("TEST MESSAGE 2", 0L);
        doReturn(Optional.of(comment1)).when(repository).findById(0L);
        doReturn(comment2).when(repository).save(Mockito.any());
        mockMvc.perform(put("/api/comments/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(comment2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("TEST MESSAGE 2")));
    }
}
