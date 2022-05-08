package com.example.springsocial.api.likes;

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
public class LikesControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeRepository repository;

    @WithMockUser
    @Test
    public void createLikeTest() throws Exception {
        Like like = LikeMock("ID", "FILM_ID", 0L);
        doReturn(like).when(repository).save(Mockito.any());
        mockMvc.perform(post("/api/likes/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(like)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @WithMockUser
    @Test
    public void getLikesTest() throws Exception {
        Like like1 = LikeMock("ID_1", "FILM_ID", 0L);
        Like like2 = LikeMock("ID_2", "FILM_ID", 0L);
        doReturn(Lists.newArrayList(like1, like2)).when(repository).findAll();
        mockMvc.perform(get("/api/likes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @WithMockUser
    @Test
    public void getLikeByIdTest() throws Exception {
        Like like = LikeMock("ID", "FILM_ID", 0L);
        doReturn(Optional.of(like)).when(repository).findById("ID");
        mockMvc.perform(get("/api/likes/ID"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("ID")));
    }
}
