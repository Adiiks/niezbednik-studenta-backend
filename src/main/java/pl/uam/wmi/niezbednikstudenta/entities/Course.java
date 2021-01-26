package pl.uam.wmi.niezbednikstudenta.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name of the course is required")
    private String name;

    @OneToOne(mappedBy = "course", cascade = CascadeType.REMOVE)
    private CourseInformation courseInformation;

    @ManyToMany(mappedBy = "courses")
    @JsonIgnore
    private Set<User> users = new LinkedHashSet<>();

    @OneToOne(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonIgnore
    private Forum forum;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean isUserBelong;

    @JsonIgnore
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<File> materials = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<PrivateLessons> privateLessons = new ArrayList<>();

    public Course() {
        forum = new Forum(this);
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

    public CourseInformation getCourseInformation() {
        return courseInformation;
    }

    public void setCourseInformation(CourseInformation courseInformation) {
        this.courseInformation = courseInformation;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public boolean isUserBelong() {
        return isUserBelong;
    }

    public void setUserBelong(boolean userBelong) {
        isUserBelong = userBelong;
    }

    public List<File> getMaterials() {
        return materials;
    }

    public void setMaterials(List<File> materials) {
        this.materials = materials;
    }

    public void addFileToMaterials(File file) {
        this.materials.add(file);
        file.setCourse(this);
    }

    public List<PrivateLessons> getPrivateLessons() {
        return privateLessons;
    }

    public void setPrivateLessons(List<PrivateLessons> privateLessons) {
        this.privateLessons = privateLessons;
    }

    public void addPrivateLessons(PrivateLessons privateLessons) {
        this.privateLessons.add(privateLessons);
        privateLessons.setCourse(this);
    }

    public void removePrivateLessons(PrivateLessons privateLessons) {
        this.privateLessons.remove(privateLessons);
        privateLessons.setCourse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) &&
                Objects.equals(name, course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
