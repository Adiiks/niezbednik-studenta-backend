package pl.uam.wmi.niezbednikstudenta.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Forum {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "course_id")
    @MapsId
    @JsonIgnore
    private Course course;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.MERGE)
    private List<Post> posts = new ArrayList<>();

    public Forum() {
    }

    public Forum(Course course) {
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post) {
        this.posts.add(post);
        post.setForum(this);
    }
}
