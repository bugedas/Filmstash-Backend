package com.example.springsocial.api.watchingNow;

import com.example.springsocial.api.films.Film;
import com.example.springsocial.api.likes.Like;
import com.example.springsocial.api.likes.LikeRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class WatchingNowControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WatchingNowRepository repository;

    @WithMockUser
    @Test
    public void createWatchingNowTest() throws Exception {
        WatchingNow watchingNow1 = WatchingNowMock(0L, "ID1", 0L, true, false, 1, 1);
        doReturn(watchingNow1).when(repository).save(Mockito.any());
        mockMvc.perform(post("/api/watching-now/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(watchingNow1)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @WithMockUser
    @Test
    public void getWatchingNowTest() throws Exception {
        WatchingNow watchingNow1 = WatchingNowMock(0L, "ID1", 0L, true, false, 1, 1);
        WatchingNow watchingNow2 = WatchingNowMock(1L, "ID2", 0L, true, false, 1, 2);
        doReturn(Lists.newArrayList(watchingNow1, watchingNow2)).when(repository).findAll();
        mockMvc.perform(get("/api/watching-now"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @WithMockUser
    @Test
    public void getWatchingNowByIdTest() throws Exception {
        WatchingNow watchingNow1 = WatchingNowMock(0L, "ID1", 0L, true, false, 1, 1);
        doReturn(Optional.of(watchingNow1)).when(repository).findById(0L);
        mockMvc.perform(get("/api/watching-now/id/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(0)));
    }

    @WithMockUser
    @Test
    public void getWatchingNowByUserIdTest() throws Exception {
        WatchingNow watchingNow1 = WatchingNowMock(0L, "ID1", 0L, true, false, 1, 1);
        WatchingNow watchingNow2 = WatchingNowMock(1L, "ID2", 0L, true, true, 1, 2);
        WatchingNow watchingNow3 = WatchingNowMock(2L, "ID1", 1L, false, true, 3, 1);
        WatchingNow watchingNow4 = WatchingNowMock(3L, "ID1", 2L, true, false, 2, 1);
        doReturn(Lists.newArrayList(watchingNow1, watchingNow2, watchingNow3, watchingNow4)).when(repository).findAll();
        mockMvc.perform(get("/api/watching-now/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId", is(0)))
                .andExpect(jsonPath("$[1].userId", is(0)));
    }

    @WithMockUser
    @Test
    public void getWatchingNowByUserAndTvTest() throws Exception {
        WatchingNow watchingNow1 = WatchingNowMock(0L, "ID1", 0L, true, false, 1, 1);
        WatchingNow watchingNow2 = WatchingNowMock(1L, "ID2", 0L, true, true, 1, 2);
        WatchingNow watchingNow3 = WatchingNowMock(2L, "ID1", 1L, false, true, 3, 1);
        WatchingNow watchingNow4 = WatchingNowMock(3L, "ID1", 2L, true, false, 2, 1);
        doReturn(Lists.newArrayList(watchingNow1, watchingNow2, watchingNow3, watchingNow4)).when(repository).findAll();
        mockMvc.perform(get("/api/watching-now/0/ID1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", is(0)))
                .andExpect(jsonPath("$.tvId", is("ID1")))
                .andExpect(jsonPath("$.id", is(0)));
    }

    @WithMockUser
    @Test
    public void updateWatchingNowTest() throws Exception {
        WatchingNow watchingNow1 = WatchingNowMock(0L, "ID1", 0L, true, false, 1, 1);
        WatchingNow watchingNow2 = WatchingNowMock(0L, "ID1", 0L, true, false, 1, 2);
        doReturn(Optional.of(watchingNow1)).when(repository).findById(0L);
        doReturn(watchingNow2).when(repository).save(Mockito.any());
        mockMvc.perform(put("/api/watching-now/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(watchingNow2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.episodeNr", is(2)));
    }
}
