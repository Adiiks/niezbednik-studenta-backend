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
import org.springframework.data.domain.*;
import pl.uam.wmi.niezbednikstudenta.entities.Comment;
import pl.uam.wmi.niezbednikstudenta.entities.Post;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.repositories.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentService commentService;

    Comment comment;
    User user;
    Post post;
    Pageable pageable;

    @BeforeEach
    void setUp() {

        comment = new Comment();

        user = new User();
        user.setId(1L);
        user.setName("A");
        user.setSurname("P");

        post = new Post();

        pageable = PageRequest.of(0, 5);
    }

    @Test
    void userLikeComment() {

        comment.getUsersWhoGaveLikes().add(user);

        boolean isUserLikeComment = commentService.isUserLikeComment(comment, user);

        assertEquals(true, isUserLikeComment);
    }

    @Test
    void userNotLikeComment() {

        boolean isUserLikeComment = commentService.isUserLikeComment(comment, user);

        assertEquals(false, isUserLikeComment);
    }

    @Test
    void save() {

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment commentSaved = commentService.save(comment);

        assertNotNull(commentSaved);
    }

    @Test
    void findAllByPost() {

        comment.getUsersWhoGaveLikes().add(user);

        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        Page<Comment> commentPage = new PageImpl<>(comments);

        when(commentRepository.findAllByPost(any(Post.class), any(Pageable.class))).thenReturn(commentPage);

        Page<Comment> commentPageRetrieved = commentService.findAllByPost(post, pageable, user);

        assertEquals(comments.size(), commentPageRetrieved.getContent().size());
        assertTrue(commentPageRetrieved.getContent().get(0).isUserLikedIt());
    }

    @Test
    void findById() throws NotFoundException {

        Optional<Comment> commentOptional = Optional.of(comment);

        when(commentRepository.findById(anyLong())).thenReturn(commentOptional);

        Comment commentFound = commentService.findById(1L);

        assertNotNull(commentFound);
    }

    @Test
    void addLike() {

        comment.setLikes(0);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentService.addOrRemoveLike(comment, user);

        assertEquals(true, comment.getUsersWhoGaveLikes().contains(user));
        assertEquals(1, comment.getLikes());
    }

    @Test
    void removeLike() {

        comment.getUsersWhoGaveLikes().add(user);
        comment.setLikes(1);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentService.addOrRemoveLike(comment, user);

        assertEquals(false, comment.getUsersWhoGaveLikes().contains(user));
        assertEquals(0, comment.getLikes());
    }

    @Test
    void acceptAnswer() {

        comment.setIsAnswer(1);
        post.setAuthor(user);
        comment.setPost(post);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentService.acceptAnswer(comment, user);

        assertEquals(1, comment.getIsAcceptedAnswer());
    }

    @Test
    void undoAcceptAnswer() {

        comment.setIsAnswer(1);
        comment.setIsAcceptedAnswer(1);
        post.setAuthor(user);
        comment.setPost(post);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentService.acceptAnswer(comment, user);

        assertEquals(0, comment.getIsAcceptedAnswer());
    }
}