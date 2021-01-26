package pl.uam.wmi.niezbednikstudenta.dtos;

import java.time.LocalDateTime;

public class PostDTO {

    private Long id;
    private LocalDateTime date;
    private UserDTO author;
    private String content;
    private int likes;
    private int totalComments;
    private boolean userLikedIt;
    private boolean edited;
    private Long courseId;

    public PostDTO(Long id, LocalDateTime date, UserDTO author, String content, int likes, int totalComments, boolean edited, Long courseId) {
        this.id = id;
        this.date = date;
        this.author = author;
        this.content = content;
        this.likes = likes;
        this.totalComments = totalComments;
        this.edited = edited;
        this.courseId = courseId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
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

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public boolean isUserLikedIt() {
        return userLikedIt;
    }

    public void setUserLikedIt(boolean userLikedIt) {
        this.userLikedIt = userLikedIt;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
