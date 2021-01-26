package pl.uam.wmi.niezbednikstudenta.dtos;

import java.time.LocalDateTime;

public class PrivateLessonsDTO {

    private Long id;
    private UserDTO author;
    private CourseDTO course;
    private String type;
    private String content;
    private LocalDateTime createdDate;
    private boolean isEdited;
    private boolean isUserInterested = false;

    public PrivateLessonsDTO() {
    }

    public PrivateLessonsDTO(Long id, UserDTO author, CourseDTO course, String type, String content, LocalDateTime createdDate, boolean isEdited) {
        this.id = id;
        this.author = author;
        this.course = course;
        this.type = type;
        this.content = content;
        this.createdDate = createdDate;
        this.isEdited = isEdited;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
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

    public boolean isUserInterested() {
        return isUserInterested;
    }

    public void setUserInterested(boolean userInterested) {
        isUserInterested = userInterested;
    }
}
