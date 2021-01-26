package pl.uam.wmi.niezbednikstudenta.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PrivateLessons {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @NotNull
    private User author;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "course_id")
    @NotNull
    private Course course;

    @NotBlank
    private String type;

    @NotBlank
    private String content;

    private LocalDateTime createdDate;
    private boolean isEdited = false;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_interest_private_lessons",
            joinColumns = @JoinColumn(name = "private_lessons_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userInterested = new ArrayList<>();

    public PrivateLessons() {
    }

    public PrivateLessons(String content, String type) {
        this.content = content;
        this.type = type;
        this.createdDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public List<User> getUserInterested() {
        return userInterested;
    }

    public void setUserInterested(List<User> userInterested) {
        this.userInterested = userInterested;
    }

    public void addUserInterested(User user) {
        this.userInterested.add(user);
        user.getPrivateLessonsInterested().add(this);
    }

    public void removeUserInterested(User user) {
        this.userInterested.remove(user);
        user.getPrivateLessonsInterested().remove(this);
    }
}
