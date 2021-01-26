package pl.uam.wmi.niezbednikstudenta.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.uam.wmi.niezbednikstudenta.entities.Coordinator;
import pl.uam.wmi.niezbednikstudenta.filter.CoordinatorFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorSpecification implements Specification<Coordinator> {

    private CoordinatorFilter coordinatorFilter;

    public CoordinatorSpecification(CoordinatorFilter coordinatorFilter) {
        this.coordinatorFilter = coordinatorFilter;
    }

    @Override
    public Predicate toPredicate(Root<Coordinator> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        criteriaQuery.distinct(true);

        if (coordinatorFilter.getName() != null && coordinatorFilter.getSurname() != null) {

            if (coordinatorFilter.getName().equals(coordinatorFilter.getSurname())) {

                Predicate predicateForName = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + coordinatorFilter.getName().toLowerCase() + "%");
                Predicate predicateForSurname = criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + coordinatorFilter.getSurname().toLowerCase() + "%");
                predicates.add(criteriaBuilder.or(predicateForName, predicateForSurname));
            }
            else
            {

                Predicate predicateForName1 = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + coordinatorFilter.getName().toLowerCase() + "%");
                Predicate predicateForName2 = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + coordinatorFilter.getSurname().toLowerCase() + "%");
                Predicate predicateOrNames = criteriaBuilder.or(predicateForName1, predicateForName2);

                Predicate predicateForSurname1 = criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + coordinatorFilter.getName().toLowerCase() + "%");
                Predicate predicateForSurname2 = criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + coordinatorFilter.getSurname().toLowerCase() + "%");
                Predicate predicateOrSurnames = criteriaBuilder.or(predicateForSurname1, predicateForSurname2);

                predicates.add(criteriaBuilder.and(predicateOrNames, predicateOrSurnames));
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
