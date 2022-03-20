package kz.bakhytzhan.security.repositories;

import kz.bakhytzhan.security.models.Comment;
import kz.bakhytzhan.security.models.Person;
import kz.bakhytzhan.security.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepositories extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long id);
    List<Comment> findAllByPerson(Person person);
    List<Comment> findAllByPost(Post post);
}
