package kz.bakhytzhan.security.dao;

import kz.bakhytzhan.security.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM person", new UserMapper());
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id=?", new Object[]{id},
                new UserMapper()).stream().findAny().orElse(null);
    }
    public void update(int id, Person person1) {
        jdbcTemplate.update("UPDATE person SET name=?, surname=?, email=? WHERE id=?", person1.getName(),
                person1.getSurname(), person1.getEmail(), id);
    }
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
    }

}
