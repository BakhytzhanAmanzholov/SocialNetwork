package kz.bakhytzhan.security.services;

import kz.bakhytzhan.security.models.*;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Person saveUser(Person user);
    Role saveRole(Role role);
    void addRoleToUser(String email, String roleName);
    Person getUser(String email);
    List<Person>getUsers();


    void addRequestFriend(String emailSender, String emailReceiver);
    List<FriendRequest>getFriendRequest(String emailReceiver);
    void accept(String idReceiver, String idSender);
    void decline(String idReceiver, String idSender);

    Post savePost(Post post);
    List<Post> getAllPosts();
    Post getPost(Long id);
    List<Post> getAllPostsByUser(Person person);

    Comment saveComment(Comment comment);
    List<Comment> getAllCommentByPost(Post post);
    Comment getComment(Long id);



}
