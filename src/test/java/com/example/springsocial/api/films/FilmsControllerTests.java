package com.example.springsocial.api.films;

import com.example.springsocial.api.comments.Comment;
import com.example.springsocial.api.comments.CommentRepository;
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

import static com.example.springsocial.api.Mocks.FilmMock;
import static com.example.springsocial.api.Mocks.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmRepository repository;

    @WithMockUser
    @Test
    public void addFilmTest() throws Exception {
        Film film = FilmMock();
        doReturn(film).when(repository).save(Mockito.any());
        mockMvc.perform(post("/api/films/film/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(film)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @WithMockUser
    @Test
    public void getFilmsTest() throws Exception {
        Film film1 = FilmMock();
        Film film2 = FilmMock("TEST 2");
        doReturn(Lists.newArrayList(film1, film2)).when(repository).findAll();
        mockMvc.perform(get("/api/films"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @WithMockUser
    @Test
    public void getFilmByIdTest() throws Exception {
        Film film1 = FilmMock();
        doReturn(Optional.of(film1)).when(repository).findById(0L);
        mockMvc.perform(get("/api/films/film/id/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.filmId", is("TESTID")));
    }

    @WithMockUser
    @Test
    public void getFilmsByUserTest() throws Exception {
        Film film1 = FilmMock("WATCHED", "TV", 0L);
        Film film2 = FilmMock("WATCH-LATER", "MOVIE", 0L);
        Film film3 = FilmMock("WATCH-LATER", "MOVIE", 1L);
        doReturn(Lists.newArrayList(film1, film2, film3)).when(repository).findAll();
        mockMvc.perform(get("/api/films/film/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId", is(0)))
                .andExpect(jsonPath("$[1].userId", is(0)));
    }

    @WithMockUser
    @Test
    public void getFilmByUserAndFilmTest() throws Exception {
        Film film1 = FilmMock("WATCHED", "TV", 0L, "0");
        Film film2 = FilmMock("WATCH-LATER", "MOVIE", 0L, "1");
        Film film3 = FilmMock("WATCH-LATER", "MOVIE", 1L, "0");
        doReturn(Lists.newArrayList(film1, film2, film3)).when(repository).findAll();
        mockMvc.perform(get("/api/films/film/0/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", is(0)))
                .andExpect(jsonPath("$.listType", is("WATCHED")));
    }

    @WithMockUser
    @Test
    public void updateFilmTest() throws Exception {
        Film film1 = FilmMock("WATCHED", "TV", 0L, "0", 0L);
        Film film2 = FilmMock("WATCH-LATER", "TV", 0L, "0", 0L);
        doReturn(Optional.of(film1)).when(repository).findById(0L);
        doReturn(film2).when(repository).save(Mockito.any());
        mockMvc.perform(put("/api/films/film/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(film2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.listType", is("WATCH-LATER")));
    }
}
