package pl.uam.wmi.niezbednikstudenta.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String contentEn;
    private LocalDateTime createdTime;
    private String type;
    private Long idOfObjectInvolve;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "notification_user",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    public Notification() {
    }

    public Notification(String content, String type) {
        this.content = content;
        this.type = type;
        this.createdTime = LocalDateTime.now();
    }

    public Notification(String content, String contentEn, String type, Long idOfObjectInvolve) {
        this.content = content;
        this.contentEn = contentEn;
        this.type = type;
        this.idOfObjectInvolve = idOfObjectInvolve;
        this.createdTime = LocalDateTime.now();
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
        user.setUserReadNotifications(false);
        user.getNotifications().add(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getNotifications().remove(this);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return content.equals(that.content) &&
                type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, type);
    }
}
