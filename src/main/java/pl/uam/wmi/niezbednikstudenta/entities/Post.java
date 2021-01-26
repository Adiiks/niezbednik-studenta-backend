package pl.uam.wmi.niezbednikstudenta.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@JsonIgnoreProperties({"comments", "forum", "usersWhoGaveLikes"})
public class Post {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "forum_id")
    private Forum forum;

    private LocalDateTime date;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "author_id")
    private User author;

    @Lob
    @NotBlank(message = "Post have to has content !")
    private String content;

    private int likes = 0;

    private int totalComments = 0;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "post_like_user", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> usersWhoGaveLikes = new LinkedHashSet<>();

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean userLikedIt;

    @Column(name = "is_edited")
    private boolean edited = false;

    public Post() {
        date = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Set<User> getUsersWhoGaveLikes() {
        return usersWhoGaveLikes;
    }

    public void setUsersWhoGaveLikes(Set<User> usersWhoGaveLikes) {
        this.usersWhoGaveLikes = usersWhoGaveLikes;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void addComment(Comment comment) {
        comment.setPost(this);
        this.comments.add(comment);
        this.totalComments += 1;
    }

    public boolean isUserLikedIt() {
        return userLikedIt;
    }

    public void setUserLikedIt(boolean userLikedIt) {
        this.userLikedIt = userLikedIt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
