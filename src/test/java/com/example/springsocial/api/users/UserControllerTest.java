package com.example.springsocial.api.users;

import com.example.springsocial.api.comments.Comment;
import com.example.springsocial.api.comments.CommentRepository;
import com.example.springsocial.api.follows.Follow;
import com.example.springsocial.api.follows.FollowRepository;
import com.example.springsocial.api.posts.Post;
import com.example.springsocial.api.posts.PostRepository;
import com.example.springsocial.api.watchingNow.WatchingNow;
import com.example.springsocial.api.watchingNow.WatchingNowRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FollowRepository followRepository;

    @MockBean
    private PostRepository postsRepository;

    @MockBean
    private CommentRepository commentsRepository;

    @MockBean
    private WatchingNowRepository watchingNowRepository;

//    @WithMockUser()
//    @Test
//    public void getCurrentUserTest() throws Exception {
//        User user = UserMock(0L, "TestName", "test@email.com");
//        doReturn(Optional.of(user)).when(userRepository).findById(Mockito.any());
//        mockMvc.perform(get("/api/user/me").with(user("user")))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name", is("TestName")));
//    }

    @WithMockUser
    @Test
    public void getUserTest() throws Exception {
        User user = UserMock(0L, "TestName", "test@email.com");
        doReturn(Optional.of(user)).when(userRepository).findById(0L);
        mockMvc.perform(get("/api/user/id/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("TestName")));
    }

//    @WithMockUser
//    @Test
//    public void updateUserTest() throws Exception {
//        User user1 = UserMock(0L, "TestName", "test@email.com");
//        User user2 = UserMock(0L, "TestNameChanged", "test@email.com");
//        doReturn(Optional.of(user1)).when(userRepository).findById(Mockito.any());
//        doReturn(user2).when(userRepository).save(Mockito.any());
//        mockMvc.perform(put("/api/user")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(user2)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name", is("TestNameChanged")));
//    }

    @WithMockUser
    @Test
    public void getUserSuggestionsTest() throws Exception {
        User user1 = UserMock(0L, "TestName", "test@email.com");
        User user2 = UserMock(1L, "TestName1", "test@email1.com");
        User user3 = UserMock(2L, "Test", "test@email2.com");
        User user4 = UserMock(3L, "Name", "test@email3.com");
        doReturn(Lists.newArrayList(user1, user2, user3, user4)).when(userRepository).findAll();
        mockMvc.perform(get("/api/user/test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @WithMockUser
    @Test
    public void getUsersTest() throws Exception {
        User user1 = UserMock(0L, "TestName", "test@email.com");
        User user2 = UserMock(1L, "TestName1", "test@email1.com");
        doReturn(Lists.newArrayList(user1, user2)).when(userRepository).findAll();
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @WithMockUser
    @Test
    public void getUserMetricsTest() throws Exception {
        Follow follow1 = FollowMock(0L, 0L, 1L);
        Follow follow2 = FollowMock(1L, 0L, 2L);
        Follow follow3 = FollowMock(2L, 1L, 0L);
        Follow follow4 = FollowMock(3L, 2L, 0L);
        doReturn(Lists.newArrayList(follow1, follow2, follow3, follow4)).when(followRepository).findAll();

        WatchingNow watchingNow1 = WatchingNowMock(0L, "ID1", 0L, true, false, 1, 1);
        WatchingNow watchingNow2 = WatchingNowMock(1L, "ID2", 0L, true, false, 1, 2);
        doReturn(Lists.newArrayList(watchingNow1, watchingNow2)).when(watchingNowRepository).findAll();

        Post post1 = PostMock(0L, "FILM_ID", 0L, "Test message", "tv", 10);
        Post post2 = PostMock(1L, "FILM_ID", 1L, "Test message 2", "tv", 5);
        Post post3 = PostMock(2L, "FILM_ID_2", 0L, "Test message 3", "movie", 5);
        doReturn(Lists.newArrayList(post1, post2, post3)).when(postsRepository).findAll();

        Comment comment1 = CommentMock("TEST MESSAGE", 0L, 0L);
        Comment comment2 = CommentMock("TEST MESSAGE 2", 0L, 0L);
        Comment comment3 = CommentMock("TEST MESSAGE 3", 0L, 1L);
        doReturn(Lists.newArrayList(comment1, comment2, comment3)).when(commentsRepository).findAll();


        mockMvc.perform(get("/api/user/metrics/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.followingCount", is(2)))
                .andExpect(jsonPath("$.followersCount", is(2)))
                .andExpect(jsonPath("$.watchingTvSeriesCount", is(2)))
                .andExpect(jsonPath("$.watchedTvSeriesCount", is(0)))
                .andExpect(jsonPath("$.myPostsCount", is(2)))
                .andExpect(jsonPath("$.myPostsLikeCount", is(15)))
                .andExpect(jsonPath("$.myCommentsCount", is(0)));
    }
}
