package pl.uam.wmi.niezbednikstudenta.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String nameEn;

    @ManyToMany(mappedBy = "terms")
    @JsonIgnore
    private List<CourseInformation> coursesInformation = new ArrayList<>();

    public Term() {
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

    public List<CourseInformation> getCoursesInformation() {
        return coursesInformation;
    }

    public void setCoursesInformation(List<CourseInformation> coursesInformation) {
        this.coursesInformation = coursesInformation;
    }
}
