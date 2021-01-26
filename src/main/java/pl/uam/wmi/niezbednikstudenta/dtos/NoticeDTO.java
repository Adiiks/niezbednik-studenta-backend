package pl.uam.wmi.niezbednikstudenta.dtos;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class NoticeDTO {

    private Long id;
    private boolean isEdited;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    private LocalDateTime createdDate;

    private UserDTO author;

    public NoticeDTO() {
    }

    public NoticeDTO(Long id, String content, LocalDateTime createdDate, UserDTO author, boolean isEdited) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
        this.author = author;
        this.isEdited = isEdited;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }
}
