package kz.bakhytzhan.security.repositories;

import kz.bakhytzhan.security.models.Person;
import kz.bakhytzhan.security.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepositories extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
    Post findByTitle(String title);
    List<Post> findAllByPerson(Person person);
}
