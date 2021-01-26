package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.dtos.CommentUpdateDTO;
import pl.uam.wmi.niezbednikstudenta.entities.Comment;
import pl.uam.wmi.niezbednikstudenta.entities.Post;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.repositories.CommentRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Page<Comment> findAllByPost(Post post, Pageable pageable, User user) {

        Page<Comment> comments = commentRepository.findAllByPost(post, pageable);

        for (Comment comment : comments.getContent()) {
            if (comment.getUsersWhoGaveLikes().contains(user))
                comment.setUserLikedIt(true);
        }

        return comments;
    }

    public Comment findById(Long commentId) throws NotFoundException {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent())
            return comment.get();
        else
            throw new NotFoundException("Comment not found. Wrong id");
    }

    public void addOrRemoveLike(Comment comment, User user) {
        if (!comment.getUsersWhoGaveLikes().contains(user)) {
            comment.getUsersWhoGaveLikes().add(user);
            comment.setLikes(comment.getLikes() + 1);
        }
        else {
            comment.getUsersWhoGaveLikes().remove(user);
            comment.setLikes(comment.getLikes() - 1);
        }
        commentRepository.save(comment);
    }

    public void acceptAnswer(Comment comment, User user) {
        Post post = comment.getPost();
        if (post.getAuthor().equals(user))
        {
            if (comment.getIsAcceptedAnswer() == 0)
                comment.setIsAcceptedAnswer(1);
            else
                comment.setIsAcceptedAnswer(0);

            commentRepository.save(comment);
        }
    }

    public boolean isUserLikeComment(Comment comment, User user) {
        return comment.getUsersWhoGaveLikes().contains(user);
    }

    public Long deleteCommentById(Long commentId, User user) throws NotFoundException {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found. Wrong id"));

        if (user.isAdmin() || comment.getAuthor().equals(user)) {
            Post post = comment.getPost();
            post.setTotalComments(post.getTotalComments() - 1);
            commentRepository.deleteById(commentId);
        }

        return comment.getId();
    }

    @Transactional
    public Comment updateComment(Long commentId, CommentUpdateDTO commentUpdate, User user) throws NotFoundException {

        Comment commentToUpdate = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found. Wrong id"));

        if (!commentToUpdate.getAuthor().equals(user))
            throw new SecurityException("You can't edit comment. You are not author");

        commentToUpdate.setContent(commentUpdate.getContent());
        commentToUpdate.setEdited(true);

        return commentRepository.save(commentToUpdate);
    }
}
