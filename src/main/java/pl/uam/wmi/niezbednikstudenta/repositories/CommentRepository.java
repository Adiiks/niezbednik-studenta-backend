package pl.uam.wmi.niezbednikstudenta.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uam.wmi.niezbednikstudenta.entities.Comment;
import pl.uam.wmi.niezbednikstudenta.entities.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Page<Comment> findAllByPost(Post post, Pageable pageable);
}
