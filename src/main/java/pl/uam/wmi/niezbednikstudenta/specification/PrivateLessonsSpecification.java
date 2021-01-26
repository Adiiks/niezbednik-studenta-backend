package pl.uam.wmi.niezbednikstudenta.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.uam.wmi.niezbednikstudenta.entities.Course;
import pl.uam.wmi.niezbednikstudenta.entities.PrivateLessons;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.filter.PrivateLessonsFilter;
import pl.uam.wmi.niezbednikstudenta.filter.UserFilter;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class PrivateLessonsSpecification implements Specification<PrivateLessons> {

    private PrivateLessonsFilter privateLessonsFilter;

    public PrivateLessonsSpecification(PrivateLessonsFilter privateLessonsFilter) {
        this.privateLessonsFilter = privateLessonsFilter;
    }

    @Override
    public Predicate toPredicate(Root<PrivateLessons> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        criteriaQuery.distinct(true);

        UserFilter userFilter = privateLessonsFilter.getUserFilter();
        if (userFilter != null) {

            Join<User, PrivateLessons> userPrivateLessonsJoin = root.join("author");

            if (userFilter.getName() != null && userFilter.getSurname() != null) {

                if (userFilter.getName().equals(userFilter.getSurname())) {

                    Predicate predicateForName = criteriaBuilder.like(criteriaBuilder.lower(userPrivateLessonsJoin.get("name")), "%" + userFilter.getName().toLowerCase() + "%");
                    Predicate predicateForSurname = criteriaBuilder.like(criteriaBuilder.lower(userPrivateLessonsJoin.get("surname")), "%" + userFilter.getSurname().toLowerCase() + "%");
                    predicates.add(criteriaBuilder.or(predicateForName, predicateForSurname));
                }
                else
                {

                    Predicate predicateForName1 = criteriaBuilder.like(criteriaBuilder.lower(userPrivateLessonsJoin.get("name")), "%" + userFilter.getName().toLowerCase() + "%");
                    Predicate predicateForName2 = criteriaBuilder.like(criteriaBuilder.lower(userPrivateLessonsJoin.get("name")), "%" + userFilter.getSurname().toLowerCase() + "%");
                    Predicate predicateOrNames = criteriaBuilder.or(predicateForName1, predicateForName2);

                    Predicate predicateForSurname1 = criteriaBuilder.like(criteriaBuilder.lower(userPrivateLessonsJoin.get("surname")), "%" + userFilter.getName().toLowerCase() + "%");
                    Predicate predicateForSurname2 = criteriaBuilder.like(criteriaBuilder.lower(userPrivateLessonsJoin.get("surname")), "%" + userFilter.getSurname().toLowerCase() + "%");
                    Predicate predicateOrSurnames = criteriaBuilder.or(predicateForSurname1, predicateForSurname2);

                    predicates.add(criteriaBuilder.and(predicateOrNames, predicateOrSurnames));
                }
            }
        }

        String courseName = privateLessonsFilter.getCourseName();
        if (courseName != null && !courseName.isBlank()) {
            Join<Course, PrivateLessons> coursePrivateLessonsJoin = root.join("course");
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(coursePrivateLessonsJoin.get("name")), "%" + courseName.toLowerCase() + "%"));
        }

        String type = privateLessonsFilter.getType();
        if (type != null && !type.isBlank()) {
            predicates.add(criteriaBuilder.equal(root.get("type"), type));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
