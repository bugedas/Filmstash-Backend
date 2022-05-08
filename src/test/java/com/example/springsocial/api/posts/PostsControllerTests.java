package com.example.springsocial.api.posts;

import com.example.springsocial.api.users.User;
import com.example.springsocial.api.users.UserRepository;
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

import static com.example.springsocial.api.Mocks.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostRepository repository;

    @MockBean
    private UserRepository userRepository;

    @WithMockUser
    @Test
    public void createPostTest() throws Exception {
        Post post = PostMock(0L, "FILM_ID", 0L, "Test message");
        doReturn(post).when(repository).save(Mockito.any());
        mockMvc.perform(post("/api/posts/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(post)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @WithMockUser
    @Test
    public void getPostsTest() throws Exception {
        Post post1 = PostMock(0L, "FILM_ID", 0L, "Test message");
        Post post2 = PostMock(1L, "FILM_ID", 1L, "Test message 2");
        doReturn(Lists.newArrayList(post1, post2)).when(repository).findAll();
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @WithMockUser
    @Test
    public void getPostByIdTest() throws Exception {
        Post post1 = PostMock(0L, "FILM_ID", 0L, "Test message");
        doReturn(Optional.of(post1)).when(repository).findById(0L);
        mockMvc.perform(get("/api/posts/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(0)));
    }

    @WithMockUser
    @Test
    public void getPostsByUserTest() throws Exception {
        Post post1 = PostMock(0L, "FILM_ID", 0L, "Test message");
        Post post2 = PostMock(1L, "FILM_ID", 1L, "Test message 2");
        Post post3 = PostMock(2L, "FILM_ID_2", 0L, "Test message 3");
        doReturn(Lists.newArrayList(post1, post2, post3)).when(repository).findAll();
        mockMvc.perform(get("/api/posts/user/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId", is(0)))
                .andExpect(jsonPath("$[1].userId", is(0)));
    }

    @WithMockUser
    @Test
    public void getPostsByFilmIdTest() throws Exception {
        Post post1 = PostMock(0L, "FID", 0L, "Test message", "tv");
        Post post2 = PostMock(1L, "FID", 1L, "Test message 2", "tv");
        Post post3 = PostMock(2L, "FILM_ID_2", 0L, "Test message 3", "tv");
        User user1 = UserMock(0L, "TEST_NAME", "test_email@email.com");
        User user2 = UserMock(1L, "TEST_NAME 2", "test_email_2@email.com");
        doReturn(Lists.newArrayList(post1, post2, post3)).when(repository).findAll();
        doReturn(user1).when(userRepository).getById(0L);
        doReturn(user2).when(userRepository).getById(1L);
        mockMvc.perform(get("/api/posts/film/FID/tv"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].post.filmId", is("FID")))
                .andExpect(jsonPath("$[1].post.filmId", is("FID")));
    }

    @WithMockUser
    @Test
    public void updatePostTest() throws Exception {
        Post post1 = PostMock(0L, "FILM_ID", 0L, "Test message", "tv");
        Post post2 = PostMock(0L, "FILM_ID", 1L, "Test message 2", "tv");
        doReturn(Optional.of(post1)).when(repository).findById(0L);
        doReturn(post2).when(repository).save(Mockito.any());
        mockMvc.perform(put("/api/posts/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(post2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Test message 2")));
    }
}
