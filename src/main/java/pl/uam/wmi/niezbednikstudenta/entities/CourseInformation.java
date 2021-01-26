package pl.uam.wmi.niezbednikstudenta.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CourseInformation {

    @Id
    private Long id;

    private Integer year;

    @OneToOne
    @JoinColumn(name = "course_id")
    @MapsId
    @JsonIgnore
    private Course course;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "course_studies_major", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "studies_major_id"))
    private List<StudiesMajor> studiesMajors = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "course_coordinator", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "coordinator_id"))
    private List<Coordinator> coordinators = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "course_link", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "link_id"))
    private List<Link> links = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "course_term", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "term_id"))
    private List<Term> terms = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "course_type_id")
    private CourseType courseType;

    @ElementCollection
    @CollectionTable(name = "course_degree", joinColumns = @JoinColumn(name = "course_id"))
    private List<Integer> degrees = new ArrayList<>();

    public CourseInformation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<StudiesMajor> getStudiesMajors() {
        return studiesMajors;
    }

    public void setStudiesMajors(List<StudiesMajor> studiesMajors) {
        this.studiesMajors = studiesMajors;
    }

    public List<Coordinator> getCoordinators() {
        return coordinators;
    }

    public void setCoordinators(List<Coordinator> coordinators) {
        this.coordinators = coordinators;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public List<Integer> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<Integer> degrees) {
        this.degrees = degrees;
    }

    public void addStudiesMajor(StudiesMajor studiesMajor) {
        this.studiesMajors.add(studiesMajor);
        studiesMajor.getCoursesInformation().add(this);
    }

    public void removeStudiesMajor(StudiesMajor studiesMajor) {
        this.studiesMajors.remove(studiesMajor);
        studiesMajor.getCoursesInformation().remove(this);
    }

    public void addCoordinator(Coordinator coordinator) {
        this.coordinators.add(coordinator);
        coordinator.getCoursesInformation().add(this);
    }

    public void removeCoordinator(Coordinator coordinator) {
        this.coordinators.remove(coordinator);
        coordinator.getCoursesInformation().remove(this);
    }

    public void addTerm(Term term) {
        this.terms.add(term);
        term.getCoursesInformation().add(this);
    }

    public void removeTerm(Term term) {
        this.terms.remove(term);
        term.getCoursesInformation().remove(this);
    }
}
