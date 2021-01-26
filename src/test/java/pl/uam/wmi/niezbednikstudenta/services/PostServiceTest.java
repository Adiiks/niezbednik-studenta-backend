package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.uam.wmi.niezbednikstudenta.entities.Forum;
import pl.uam.wmi.niezbednikstudenta.entities.Post;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.repositories.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @Mock
    CommentService commentService;

    @InjectMocks
    PostService postService;

    Post post;
    User user;
    Forum forum;

    @BeforeEach
    void setUp() {
        post = new Post();

        user = new User();
        user.setId(1L);
        user.setName("A");
        user.setSurname("P");

        forum = new Forum();
    }

    @Test
    void userLikePost() {

        post.getUsersWhoGaveLikes().add(user);

        boolean isUserLikePost = postService.isUserLikePost(post, user);

        assertEquals(true, isUserLikePost);
    }

    @Test
    void userNotLikePost() {

        boolean isUserLikePost = postService.isUserLikePost(post, user);

        assertEquals(false, isUserLikePost);
    }

    @Test
    void save() {

        post.setAuthor(user);
        post.setForum(forum);
        String content = "post";
        post.setContent(content);

        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post savedPost = postService.save(post);

        assertNotNull(savedPost);
        assertEquals(user, post.getAuthor());
        assertEquals(forum, post.getForum());
        assertEquals(content, post.getContent());
    }

    @Test
    void findAll() {

        post.getUsersWhoGaveLikes().add(user);

        List<Post> posts = new ArrayList<>();
        posts.add(post);

        when(postRepository.findAllByForum(any(Pageable.class), any(Forum.class))).thenReturn(new PageImpl<>(posts));

        Page<Post> postPage = postService.findAll(Pageable.unpaged(), forum, user, null);

        assertEquals(posts.size(), postPage.getContent().size());
        assertTrue(postPage.getContent().get(0).isUserLikedIt());
    }

    @Test
    void findById() throws NotFoundException {

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        assertNotNull(postService.findById(1l));
    }

    @Test
    void addLike() {
    }

    @Test
    void removeLike() {
    }

    @Test
    void addComment() {
    }

    @Test
    void getAllComments() {
    }
}