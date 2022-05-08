package com.example.springsocial.api.follows;

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
public class FollowsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FollowRepository repository;

    @MockBean
    private UserRepository userRepository;

    @WithMockUser
    @Test
    public void createFollowTest() throws Exception {
        Follow follow = FollowMock(0L, 0L, 0L);
        doReturn(follow).when(repository).save(Mockito.any());
        mockMvc.perform(post("/api/friends/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(follow)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @WithMockUser
    @Test
    public void getFollowsTest() throws Exception {
        Follow follow1 = FollowMock(0L, 0L, 0L);
        Follow follow2 = FollowMock(1L, 0L, 1L);
        doReturn(Lists.newArrayList(follow1, follow2)).when(repository).findAll();
        mockMvc.perform(get("/api/friends"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @WithMockUser
    @Test
    public void getFollowByIdTest() throws Exception {
        Follow follow1 = FollowMock(0L, 0L, 0L);
        doReturn(Optional.of(follow1)).when(repository).findById(0L);
        mockMvc.perform(get("/api/friends/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(0)));
    }

    @WithMockUser
    @Test
    public void getFollowsByUserTest() throws Exception {
        Follow follow1 = FollowMock(0L, 0L, 1L);
        Follow follow2 = FollowMock(1L, 0L, 2L);
        Follow follow3 = FollowMock(2L, 1L, 0L);
        doReturn(Lists.newArrayList(follow1, follow2, follow3)).when(repository).findAll();
        mockMvc.perform(get("/api/friends/user/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].followingId", is(0)))
                .andExpect(jsonPath("$[1].followingId", is(0)));
    }

    @WithMockUser
    @Test
    public void getFollowsByUserFullTest() throws Exception {
        Follow follow1 = FollowMock(0L, 0L, 1L);
        Follow follow2 = FollowMock(1L, 0L, 2L);
        Follow follow3 = FollowMock(2L, 1L, 0L);
        User user1 = UserMock(1L, "TEST_NAME", "test_email@email.com");
        User user2 = UserMock(2L, "TEST_NAME 2", "test_email_2@email.com");
        doReturn(Lists.newArrayList(follow1, follow2, follow3)).when(repository).findAll();
        doReturn(Optional.of(user1)).when(userRepository).findById(1L);
        doReturn(Optional.of(user2)).when(userRepository).findById(2L);
        mockMvc.perform(get("/api/friends/user/full/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("TEST_NAME")))
                .andExpect(jsonPath("$[1].name", is("TEST_NAME 2")));
    }

    @WithMockUser
    @Test
    public void updateFollowTest() throws Exception {
        Follow follow1 = FollowMock(0L, 0L, 1L);
        Follow follow2 = FollowMock(0L, 0L, 2L);
        doReturn(Optional.of(follow1)).when(repository).findById(0L);
        doReturn(follow2).when(repository).save(Mockito.any());
        mockMvc.perform(put("/api/friends/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(follow2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.followingId", is(0)));
    }
}
