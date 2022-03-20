package kz.bakhytzhan.security.dao;

import kz.bakhytzhan.security.models.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        person.setId(rs.getLong("id"));
        person.setName(rs.getString("name"));
        person.setSurname(rs.getString("surname"));
        person.setEmail(rs.getString("email"));
        person.setPassword(rs.getString("password"));
        return person;
    }
}
