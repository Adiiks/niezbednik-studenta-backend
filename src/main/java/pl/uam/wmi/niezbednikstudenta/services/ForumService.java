package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.entities.*;
import pl.uam.wmi.niezbednikstudenta.filter.PostFilter;
import pl.uam.wmi.niezbednikstudenta.repositories.ForumRepository;

import java.util.*;

@Service
public class ForumService {

    private final ForumRepository forumRepository;
    private final PostService postService;
    private final NotificationService notificationService;

    public ForumService(ForumRepository forumRepository, PostService postService, NotificationService notificationService) {
        this.forumRepository = forumRepository;
        this.postService = postService;
        this.notificationService = notificationService;
    }

    public Forum save(Forum forum) {
        return forumRepository.save(forum);
    }

    public Post addPost(Post post, Long courseId) throws NotFoundException {

        Optional<Forum> optionalForum = forumRepository.findById(courseId);
        if (optionalForum.isPresent()) {

            post.setForum(optionalForum.get());
            Post postFromDB = postService.save(post);

            Map<String, String> dataForNotification = new HashMap<>();
            Course course = postFromDB.getForum().getCourse();
            dataForNotification.put("messagePl", "Dodano nowy post w przedmiocie " + course.getName());
            dataForNotification.put("messageEn", "Added new post in course " + course.getName());
            dataForNotification.put("id", postFromDB.getId().toString());

            Set<User> usersInCourse = course.getUsers();
            usersInCourse.remove(postFromDB.getAuthor());
            usersInCourse.forEach(user -> notificationService.sendToUser(dataForNotification, user.getTokenFirebase()));

            String contentForNotification = "Użytkownik " + postFromDB.getAuthor().getName() + " " + postFromDB.getAuthor().getSurname() + " dodał post w " + course.getName();
            String contentEnForNotification = "User " + postFromDB.getAuthor().getName() + " " + postFromDB.getAuthor().getSurname() + " added new post in " + course.getName();
            Notification notification = new Notification(contentForNotification, contentEnForNotification, "POST", course.getId());
            usersInCourse.forEach(user -> notification.addUser(user));
            notificationService.saveNotification(notification);

            return postFromDB;
        } else
            throw new NotFoundException("Forum not found. Wrong id!");
    }

    public Forum findById(Long forumId) throws NotFoundException {
        Optional<Forum> forumOptional = forumRepository.findById(forumId);
        if (forumOptional.isPresent())
            return forumOptional.get();
        else
            throw new NotFoundException("Forum not found. Wrong id!");
    }

    public Page<Post> findAllPostsInForum(Pageable pageable, Long forumId, User user, PostFilter postFilter) throws NotFoundException {

        Forum forum = findById(forumId);
        return postService.findAll(pageable, forum, user, postFilter);
    }

    public void deleteForumWithCourse(Forum forum) {
        postService.deleteAllPostsWithForum(forum.getPosts());
    }
}
