package pl.uam.wmi.niezbednikstudenta.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"post", "usersWhoGaveLikes"})
public class Comment {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Range(min = 0, max = 1, message = "Property isAnswer can be only 0 or 1")
    private int isAnswer = 0;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int isAcceptedAnswer = 0;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int likes = 0;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "comment_like_user", joinColumns = @JoinColumn(name = "comment_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> usersWhoGaveLikes = new LinkedHashSet<>();

    @Lob
    @NotBlank(message = "Comment can not be blank")
    private String content;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User author;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean userLikedIt;

    private LocalDateTime date;

    @Column(name = "is_edited")
    private boolean edited = false;

    public Comment() {
        date = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIsAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(int isAnswer) {
        this.isAnswer = isAnswer;
    }

    public int getIsAcceptedAnswer() {
        return isAcceptedAnswer;
    }

    public void setIsAcceptedAnswer(int isAcceptedAnswer) {
        this.isAcceptedAnswer = isAcceptedAnswer;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<User> getUsersWhoGaveLikes() {
        return usersWhoGaveLikes;
    }

    public void setUsersWhoGaveLikes(Set<User> usersWhoGaveLikes) {
        this.usersWhoGaveLikes = usersWhoGaveLikes;
    }

    public boolean isUserLikedIt() {
        return userLikedIt;
    }

    public void setUserLikedIt(boolean userLikedIt) {
        this.userLikedIt = userLikedIt;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }
}
