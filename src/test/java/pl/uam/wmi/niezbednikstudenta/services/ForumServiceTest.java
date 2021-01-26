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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.uam.wmi.niezbednikstudenta.entities.Course;
import pl.uam.wmi.niezbednikstudenta.entities.Forum;
import pl.uam.wmi.niezbednikstudenta.entities.Post;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.filter.PostFilter;
import pl.uam.wmi.niezbednikstudenta.repositories.ForumRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ForumServiceTest {

    @Mock
    ForumRepository forumRepository;

    @Mock
    PostService postService;

    @InjectMocks
    ForumService forumService;

    Forum forum;
    Post post;
    Course course;
    User user;

    @BeforeEach
    void setUp() {

        forum = new Forum();
        post = new Post();
        post.setId(1L);

        course = new Course();
        course.setName("Algebra");

        forum.setCourse(course);

        user = new User();
        user.setName("X");
        user.setSurname("Y");
    }

    @Test
    void save() {

        when(forumRepository.save(any(Forum.class))).thenReturn(forum);

        Forum forumSaved = forumService.save(forum);

        assertNotNull(forumSaved);
        assertNotNull(forumSaved.getCourse());
    }

    @Test
    void findById() throws NotFoundException {

        when(forumRepository.findById(anyLong())).thenReturn(Optional.of(forum));

        assertNotNull(forumService.findById(1L));
    }
}