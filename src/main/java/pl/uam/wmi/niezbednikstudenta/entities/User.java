package pl.uam.wmi.niezbednikstudenta.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "User_Usos")
@JsonIgnoreProperties({"courses", "posts", "comments", "privateLessons", "tokenFirebase", "notifications", "userReadNotifications", "privateLessonsInterested"})
public class User {

    @Id
    private Long id;

    private String name;
    private String surname;
    private String email;
    private String tokenFirebase;

    private boolean isAdmin = false;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "user_course", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.DETACH, CascadeType.REFRESH}, mappedBy = "author")
    private List<PrivateLessons> privateLessons = new ArrayList<>();

    @ManyToMany(mappedBy = "users", cascade = CascadeType.MERGE)
    private List<Notification> notifications = new ArrayList<>();

    @ManyToMany(mappedBy = "userInterested", cascade = CascadeType.MERGE)
    private Set<PrivateLessons> privateLessonsInterested = new HashSet<>();

    private boolean userReadNotifications = true;

    public User() {
    }

    public User(Long id, String name, String surname, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
        course.getUsers().add(this);
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
        course.getUsers().remove(this);
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post) {
        this.posts.add(post);
        post.setAuthor(this);
    }

    public List<PrivateLessons> getPrivateLessons() {
        return privateLessons;
    }

    public void setPrivateLessons(List<PrivateLessons> privateLessons) {
        this.privateLessons = privateLessons;
    }

    public void addPrivateLessons(PrivateLessons privateLessons) {
        this.privateLessons.add(privateLessons);
        privateLessons.setAuthor(this);
    }

    public void removePrivateLessons(PrivateLessons privateLessons) {
        this.privateLessons.remove(privateLessons);
        privateLessons.setAuthor(null);
    }

    public String getTokenFirebase() {
        return tokenFirebase;
    }

    public void setTokenFirebase(String tokenFirebase) {
        this.tokenFirebase = tokenFirebase;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public boolean isUserReadNotifications() {
        return userReadNotifications;
    }

    public void setUserReadNotifications(boolean userReadNotifications) {
        this.userReadNotifications = userReadNotifications;
    }

    public Set<PrivateLessons> getPrivateLessonsInterested() {
        return privateLessonsInterested;
    }

    public void setPrivateLessonsInterested(Set<PrivateLessons> privateLessonsInterested) {
        this.privateLessonsInterested = privateLessonsInterested;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
