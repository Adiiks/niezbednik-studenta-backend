package pl.uam.wmi.niezbednikstudenta.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.uam.wmi.niezbednikstudenta.entities.Forum;
import pl.uam.wmi.niezbednikstudenta.entities.Post;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.filter.PostFilter;
import pl.uam.wmi.niezbednikstudenta.filter.UserFilter;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class PostSpecification implements Specification<Post> {

    private PostFilter postFilter;
    private Forum forum;

    public PostSpecification(PostFilter postFilter, Forum forum) {
        this.postFilter = postFilter;
        this.forum = forum;
    }

    @Override
    public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        criteriaQuery.distinct(true);

        if (forum != null) {
            Join<Forum, Post> forumPostJoin = root.join("forum");
            predicates.add(criteriaBuilder.equal(forumPostJoin.get("id"), forum.getId()));
        }

        if (postFilter.getContent() != null && !postFilter.getContent().isBlank()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), "%" + postFilter.getContent().toLowerCase() + "%"));
        }

        UserFilter userFilter = postFilter.getUserFilter();
        if (userFilter != null) {

            Join<User, Post> userPostJoin = root.join("author");

            if (userFilter.getName() != null && userFilter.getSurname() != null) {

                if (userFilter.getName().equals(userFilter.getSurname())) {

                    Predicate predicateForName = criteriaBuilder.like(criteriaBuilder.lower(userPostJoin.get("name")), "%" + userFilter.getName().toLowerCase() + "%");
                    Predicate predicateForSurname = criteriaBuilder.like(criteriaBuilder.lower(userPostJoin.get("surname")), "%" + userFilter.getSurname().toLowerCase() + "%");
                    predicates.add(criteriaBuilder.or(predicateForName, predicateForSurname));
                }
                else
                {

                    Predicate predicateForName1 = criteriaBuilder.like(criteriaBuilder.lower(userPostJoin.get("name")), "%" + userFilter.getName().toLowerCase() + "%");
                    Predicate predicateForName2 = criteriaBuilder.like(criteriaBuilder.lower(userPostJoin.get("name")), "%" + userFilter.getSurname().toLowerCase() + "%");
                    Predicate predicateOrNames = criteriaBuilder.or(predicateForName1, predicateForName2);

                    Predicate predicateForSurname1 = criteriaBuilder.like(criteriaBuilder.lower(userPostJoin.get("surname")), "%" + userFilter.getName().toLowerCase() + "%");
                    Predicate predicateForSurname2 = criteriaBuilder.like(criteriaBuilder.lower(userPostJoin.get("surname")), "%" + userFilter.getSurname().toLowerCase() + "%");
                    Predicate predicateOrSurnames = criteriaBuilder.or(predicateForSurname1, predicateForSurname2);

                    predicates.add(criteriaBuilder.and(predicateOrNames, predicateOrSurnames));
                }
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
