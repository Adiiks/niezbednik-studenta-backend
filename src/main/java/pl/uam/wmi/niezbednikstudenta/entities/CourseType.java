package pl.uam.wmi.niezbednikstudenta.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CourseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String nameEn;

    @OneToMany(mappedBy = "courseType")
    @JsonIgnore
    private List<CourseInformation> courses = new ArrayList<>();

    public CourseType() {
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

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public List<CourseInformation> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseInformation> courses) {
        this.courses = courses;
    }
}
