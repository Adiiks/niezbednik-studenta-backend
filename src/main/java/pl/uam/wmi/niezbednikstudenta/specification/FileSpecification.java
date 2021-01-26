package pl.uam.wmi.niezbednikstudenta.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.uam.wmi.niezbednikstudenta.entities.Course;
import pl.uam.wmi.niezbednikstudenta.entities.File;
import pl.uam.wmi.niezbednikstudenta.filter.FileFilter;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class FileSpecification implements Specification<File> {

    private FileFilter fileFilter;
    private Course course;

    public FileSpecification(FileFilter fileFilter, Course course) {
        this.fileFilter = fileFilter;
        this.course = course;
    }

    @Override
    public Predicate toPredicate(Root<File> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        criteriaQuery.distinct(true);

        if (course != null) {
            Join<Course, File> courseFileJoin = root.join("course");
            predicates.add(criteriaBuilder.equal(courseFileJoin.get("id"), course.getId()));
        }

        if (fileFilter.getFileName() != null && !fileFilter.getFileName().isBlank()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + fileFilter.getFileName().toLowerCase() + "%"));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
