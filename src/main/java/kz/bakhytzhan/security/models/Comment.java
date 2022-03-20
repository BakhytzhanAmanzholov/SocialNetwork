package kz.bakhytzhan.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String str;
    private Date today = new Date();
    @ManyToOne
    private Person person;
    @ManyToOne
    private Post post;
    private String sub = today.toString().substring(0,16);

    public Comment(Long id, String str, Person person, Post post) {
        this.id = id;
        this.str = str;
        this.person = person;
        this.post = post;
    }
}
