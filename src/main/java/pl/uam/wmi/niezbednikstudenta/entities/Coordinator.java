package pl.uam.wmi.niezbednikstudenta.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Coordinator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    private String page;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "coordinator_degree_id")
    @NotNull(message = "Coordinator's degree cannot be null")
    private CoordinatorDegree coordinatorDegree;

    @Lob
    private String information;

    @ManyToMany(mappedBy = "coordinators")
    @JsonIgnore
    private List<CourseInformation> coursesInformation = new ArrayList<>();

    public Coordinator() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CoordinatorDegree getCoordinatorDegree() {
        return coordinatorDegree;
    }

    public void setCoordinatorDegree(CoordinatorDegree coordinatorDegree) {
        this.coordinatorDegree = coordinatorDegree;
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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public List<CourseInformation> getCoursesInformation() {
        return coursesInformation;
    }

    public void setCoursesInformation(List<CourseInformation> coursesInformation) {
        this.coursesInformation = coursesInformation;
    }
}
