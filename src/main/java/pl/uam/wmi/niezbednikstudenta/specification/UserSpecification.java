package pl.uam.wmi.niezbednikstudenta.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.filter.UserFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {

    private UserFilter userFilter;

    public UserSpecification() {
    }

    public UserSpecification(UserFilter userFilter) {
        this.userFilter = userFilter;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        criteriaQuery.distinct(true);

        if (userFilter.getName() != null && userFilter.getSurname() != null) {

            if (userFilter.getName().equals(userFilter.getSurname())) {

                Predicate predicateForName = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + userFilter.getName().toLowerCase() + "%");
                Predicate predicateForSurname = criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + userFilter.getSurname().toLowerCase() + "%");
                predicates.add(criteriaBuilder.or(predicateForName, predicateForSurname));
            }
            else
            {

                Predicate predicateForName1 = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + userFilter.getName().toLowerCase() + "%");
                Predicate predicateForName2 = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + userFilter.getSurname().toLowerCase() + "%");
                Predicate predicateOrNames = criteriaBuilder.or(predicateForName1, predicateForName2);

                Predicate predicateForSurname1 = criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + userFilter.getName().toLowerCase() + "%");
                Predicate predicateForSurname2 = criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + userFilter.getSurname().toLowerCase() + "%");
                Predicate predicateOrSurnames = criteriaBuilder.or(predicateForSurname1, predicateForSurname2);

                predicates.add(criteriaBuilder.and(predicateOrNames, predicateOrSurnames));
            }
        }

        if (userFilter.getIsAdmin() == 1)
            predicates.add(criteriaBuilder.isTrue(root.get("isAdmin")));

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
