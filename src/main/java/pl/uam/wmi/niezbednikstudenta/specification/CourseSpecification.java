package pl.uam.wmi.niezbednikstudenta.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.uam.wmi.niezbednikstudenta.entities.*;
import pl.uam.wmi.niezbednikstudenta.filter.CourseFilter;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class CourseSpecification implements Specification<Course> {

    private CourseFilter courseFilter;
    private User user;

    public CourseSpecification() {
    }

    public CourseSpecification(CourseFilter courseFilter) {
        this.courseFilter = courseFilter;
    }

    public CourseSpecification(CourseFilter courseFilter, User user) {
        this.courseFilter = courseFilter;
        this.user = user;
    }

    @Override
    public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        String nameInLanguage = (courseFilter.getLanguage().equalsIgnoreCase("pl")) ? "name" : "nameEn";

        List<Predicate> predicates = new ArrayList<>();
        Join<CourseInformation, Course> courseInformationCourseJoin = root.join("courseInformation");
        criteriaQuery.distinct(true);

        if (courseFilter.getName() != null)
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + courseFilter.getName().toLowerCase() + "%"));

        if (courseFilter.getStudiesMajors() != null && !courseFilter.getStudiesMajors().isEmpty()) {
            Join<CourseInformation, StudiesMajor> courseStudiesMajorJoin = courseInformationCourseJoin.join("studiesMajors");
            predicates.add(courseStudiesMajorJoin.get(nameInLanguage).in(courseFilter.getStudiesMajors()));
        }

        if (courseFilter.getTerms() != null && !courseFilter.getTerms().isEmpty()) {
            Join<CourseInformation, Term> courseTermJoin = courseInformationCourseJoin.join("terms");
            predicates.add(courseTermJoin.get(nameInLanguage).in(courseFilter.getTerms()));
        }

        if (courseFilter.getDegrees() != null && !courseFilter.getDegrees().isEmpty()) {
            predicates.add(courseInformationCourseJoin.join("degrees").in(courseFilter.getDegrees()));
        }

        if (user != null) {
            Join<User, Course> userCourseJoin = root.join("users");
            predicates.add(criteriaBuilder.equal(userCourseJoin.get("id"), user.getId()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
