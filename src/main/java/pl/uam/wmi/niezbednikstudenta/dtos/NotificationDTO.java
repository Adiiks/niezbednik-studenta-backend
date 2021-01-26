package pl.uam.wmi.niezbednikstudenta.dtos;

import java.time.LocalDateTime;

public class NotificationDTO {

    private Long id;
    private String content;
    private String contentEn;
    private LocalDateTime createdTime;
    private String type;
    private Long idOfObjectInvolve;

    public NotificationDTO() {
    }

    public NotificationDTO(Long id, String content, LocalDateTime createdTime, String type, Long idOfObjectInvolve, String contentEn) {
        this.id = id;
        this.content = content;
        this.createdTime = createdTime;
        this.type = type;
        this.idOfObjectInvolve = idOfObjectInvolve;
        this.contentEn = contentEn;
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getIdOfObjectInvolve() {
        return idOfObjectInvolve;
    }

    public void setIdOfObjectInvolve(Long idOfObjectInvolve) {
        this.idOfObjectInvolve = idOfObjectInvolve;
    }

    public String getContentEn() {
        return contentEn;
    }

    public void setContentEn(String contentEn) {
        this.contentEn = contentEn;
    }
}
