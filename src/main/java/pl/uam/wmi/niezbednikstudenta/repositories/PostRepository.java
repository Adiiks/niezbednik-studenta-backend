package pl.uam.wmi.niezbednikstudenta.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.uam.wmi.niezbednikstudenta.entities.Forum;
import pl.uam.wmi.niezbednikstudenta.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Page<Post> findAllByForum(Pageable pageable, Forum forum);
}
