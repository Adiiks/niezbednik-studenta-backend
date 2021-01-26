package pl.uam.wmi.niezbednikstudenta.dtos;

import java.time.LocalDateTime;

public class ResponseFileDTO {

    private Long id;
    private String name;
    private String type;
    private long size;
    private UserDTO author;
    private LocalDateTime createdDate;

    public ResponseFileDTO() {
    }

    public ResponseFileDTO(Long id, String name, String type, long size, UserDTO author, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.author = author;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
