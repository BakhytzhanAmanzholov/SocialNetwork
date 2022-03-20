package kz.bakhytzhan.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String Sender;
    private String Receiver;
    private String state = "PENDING";

    public FriendRequest(String receiver) {
        Receiver = receiver;
    }

    public FriendRequest(String sender, String receiver) {
        Sender = sender;
        Receiver = receiver;
    }
}
