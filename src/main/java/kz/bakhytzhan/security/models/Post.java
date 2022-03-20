package kz.bakhytzhan.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String title;
    private String content;
    private String state = "ALL";
    private boolean ifComments;
    private Date today = new Date();
    private String sub = today.toString().substring(0,16);

    @ManyToOne
    private Person person;
    @OneToMany
    private List<Comment> comments = new ArrayList<>();

    public Post(Long id, String title, String content, String state, boolean ifComments, Person person) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.state = state;
        this.ifComments = ifComments;
        this.person = person;
    }
}
