package pl.uam.wmi.niezbednikstudenta.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.uam.wmi.niezbednikstudenta.entities.Notice;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.filter.NoticeFilter;
import pl.uam.wmi.niezbednikstudenta.filter.UserFilter;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class NoticeSpecification implements Specification<Notice> {

    private NoticeFilter noticeFilter;

    public NoticeSpecification(NoticeFilter noticeFilter) {
        this.noticeFilter = noticeFilter;
    }

    @Override
    public Predicate toPredicate(Root<Notice> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        criteriaQuery.distinct(true);

        if (noticeFilter.getContent() != null && !noticeFilter.getContent().isBlank()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), "%" + noticeFilter.getContent().toLowerCase() + "%"));
        }

        UserFilter userFilter = noticeFilter.getUserFilter();
        if (userFilter != null) {

            Join<User, Notice> userNoticeJoin = root.join("author");

            if (userFilter.getName() != null && userFilter.getSurname() != null) {

                if (userFilter.getName().equals(userFilter.getSurname())) {

                    Predicate predicateForName = criteriaBuilder.like(criteriaBuilder.lower(userNoticeJoin.get("name")), "%" + userFilter.getName().toLowerCase() + "%");
                    Predicate predicateForSurname = criteriaBuilder.like(criteriaBuilder.lower(userNoticeJoin.get("surname")), "%" + userFilter.getSurname().toLowerCase() + "%");
                    predicates.add(criteriaBuilder.or(predicateForName, predicateForSurname));
                }
                else
                {

                    Predicate predicateForName1 = criteriaBuilder.like(criteriaBuilder.lower(userNoticeJoin.get("name")), "%" + userFilter.getName().toLowerCase() + "%");
                    Predicate predicateForName2 = criteriaBuilder.like(criteriaBuilder.lower(userNoticeJoin.get("name")), "%" + userFilter.getSurname().toLowerCase() + "%");
                    Predicate predicateOrNames = criteriaBuilder.or(predicateForName1, predicateForName2);

                    Predicate predicateForSurname1 = criteriaBuilder.like(criteriaBuilder.lower(userNoticeJoin.get("surname")), "%" + userFilter.getName().toLowerCase() + "%");
                    Predicate predicateForSurname2 = criteriaBuilder.like(criteriaBuilder.lower(userNoticeJoin.get("surname")), "%" + userFilter.getSurname().toLowerCase() + "%");
                    Predicate predicateOrSurnames = criteriaBuilder.or(predicateForSurname1, predicateForSurname2);

                    predicates.add(criteriaBuilder.and(predicateOrNames, predicateOrSurnames));
                }
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
