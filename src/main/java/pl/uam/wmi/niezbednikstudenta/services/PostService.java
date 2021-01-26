package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.dtos.PostDTO;
import pl.uam.wmi.niezbednikstudenta.dtos.PostUpdateDTO;
import pl.uam.wmi.niezbednikstudenta.dtos.UserDTO;
import pl.uam.wmi.niezbednikstudenta.entities.*;
import pl.uam.wmi.niezbednikstudenta.filter.PostFilter;
import pl.uam.wmi.niezbednikstudenta.repositories.PostRepository;
import pl.uam.wmi.niezbednikstudenta.specification.PostSpecification;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentService commentService;
    private final NotificationService notificationService;

    public PostService(PostRepository postRepository, CommentService commentService, NotificationService notificationService) {
        this.postRepository = postRepository;
        this.commentService = commentService;
        this.notificationService = notificationService;
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Page<Post> findAll(Pageable pageable, Forum forum, User user, PostFilter postFilter) {

        Page<Post> posts = (postFilter == null) ? postRepository.findAllByForum(pageable, forum) : postRepository.findAll(new PostSpecification(postFilter, forum), pageable);

        for (Post post : posts.getContent()) {
            if (post.getUsersWhoGaveLikes().contains(user))
                post.setUserLikedIt(true);
        }

        return posts;
    }

    public Post findById(Long postId) throws NotFoundException {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent())
            return postOptional.get();
        else
            throw new NotFoundException("Post not found. Wrong id!");
    }

    public void addOrRemoveLike(Post post, User user) {
        if (!post.getUsersWhoGaveLikes().contains(user)) {
            post.getUsersWhoGaveLikes().add(user);
            post.setLikes(post.getLikes() + 1);
        }
        else {
            post.getUsersWhoGaveLikes().remove(user);
            post.setLikes(post.getLikes() - 1);
        }
        postRepository.save(post);
    }

    public Comment addComment(Post post, Comment comment) {

        List<Comment> comments = post.getComments();

        post.addComment(comment);
        Comment commentFromDb = commentService.save(comment);

        Map<String, String> dataForNotification = new HashMap<>();
        dataForNotification.put("messagePl", "Dodano nowy komentarz do postu");
        dataForNotification.put("messageEn", "Added new comment to post");
        dataForNotification.put("id", commentFromDb.getId().toString());

        Set<User> usersToNotify = new HashSet<>();
        User postAuthor = post.getAuthor();
        usersToNotify.add(postAuthor);
        comments.forEach(iterator -> {

            User commentAuthor = iterator.getAuthor();
            if (commentAuthor.equals(postAuthor))
                usersToNotify.remove(commentAuthor);
        });


        usersToNotify.forEach(user -> notificationService.sendToUser(dataForNotification, user.getTokenFirebase()));

        String contentForNotification = "Użytkownik " + commentFromDb.getAuthor().getName() + " " + commentFromDb.getAuthor().getSurname() + " skomentował post";
        String contentEnForNotification = "User " + commentFromDb.getAuthor().getName() + " " + commentFromDb.getAuthor().getSurname() + " commented on post";
        Notification notification = new Notification(contentForNotification, contentEnForNotification, "Komentarz", post.getId());
        usersToNotify.forEach(user -> notification.addUser(user));
        notificationService.saveNotification(notification);

        return commentFromDb;
    }

    public Page<Comment> getAllComments(Post post, Pageable pageable, User user) {
        return commentService.findAllByPost(post, pageable, user);
    }

    public boolean isUserLikePost(Post post, User user) {
        return post.getUsersWhoGaveLikes().contains(user);
    }

    public Long deletePostById(Long postId, User user) throws NotFoundException {

        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found. Wrong id"));

        if (user.isAdmin() || user.getPosts().contains(post)) {

            post.setForum(null);
            post.setAuthor(null);
            postRepository.deleteById(postId);
        }

        return post.getId();
    }

    public void deleteAllPostsWithForum(List<Post> posts) {
        posts.forEach(post -> {
            post.setForum(null);
            post.setAuthor(null);
            postRepository.deleteById(post.getId());
        });
    }

    @Transactional
    public Post updatePost(Long postId, PostUpdateDTO postUpdate, User user) throws NotFoundException {

        Post postToUpdate = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found. Wrong id"));

        if (!postToUpdate.getAuthor().equals(user))
            throw new SecurityException("Post can be edit only by his author");

        postToUpdate.setContent(postUpdate.getContent());
        postToUpdate.setEdited(true);

        return postRepository.save(postToUpdate);
    }

    public PostDTO findPostById(Long postId, User user) throws NotFoundException {

        Post postFromDb = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found. Wrong id"));
        User author = postFromDb.getAuthor();

        PostDTO postToReturn = new PostDTO(
                postFromDb.getId(),
                postFromDb.getDate(),
                new UserDTO(
                        author.getId(),
                        author.getName(),
                        author.getSurname()
                ),
                postFromDb.getContent(),
                postFromDb.getLikes(),
                postFromDb.getTotalComments(),
                postFromDb.isEdited(),
                postFromDb.getForum().getId());

        postToReturn.setUserLikedIt(postFromDb.getUsersWhoGaveLikes().contains(user));

        return postToReturn;
    }
}
