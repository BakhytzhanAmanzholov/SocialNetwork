package kz.bakhytzhan.security.services;

import kz.bakhytzhan.security.models.*;
import kz.bakhytzhan.security.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserImplementation implements UserService, UserDetailsService {
    private final UserRepositories userRepositories;
    private final RoleRepositories roleRepositories;
   private final PasswordEncoder passwordEncoder;
    private final FriendRequestRepositories friendRequestRepositories;
    private final PostRepositories postRepositories;
    private final CommentRepositories commentRepositories;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Person user = userRepositories.findByEmail(s);
        if(user == null){
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        else {
            log.info("User found {}", user.getEmail());

        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities) ;
    }
    @Override
    public Person saveUser(Person user) {
        log.info("Saving new User {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepositories.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new Role {}", role.getName());
        return roleRepositories.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        log.info("Adding Role {} to User {}", roleName, email);
        Person user = userRepositories.findByEmail(email);
        Role role = roleRepositories.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public Person getUser(String email) {
        log.info("Get User by Email {}", email);
        Person person;

        return userRepositories.findByEmail(email);
    }

    @Override
    public List<Person> getUsers() {
        log.info("Get all Users");
        return userRepositories.findAll();
    }


    @Override
    public void addRequestFriend(String emailSender, String emailReceiver) {
        Person userSender = userRepositories.findByEmail(emailSender);
        Person userReceiver = userRepositories.findByEmail(emailReceiver);
        FriendRequest friendRequest = new FriendRequest(userSender.getEmail(), userReceiver.getEmail());
        friendRequestRepositories.save(friendRequest);
    }
    @Override
    public List<FriendRequest> getFriendRequest(String emailReceiver) {
        log.info("Get all Friend Request");
        List<FriendRequest> friendRequests = friendRequestRepositories.findAll();
        List<FriendRequest> needed = new ArrayList<>();
        for(FriendRequest friendRequest: friendRequests){
            if(friendRequest.getReceiver().equals(emailReceiver) && friendRequest.getState().equals("PENDING")){
                needed.add(friendRequest);
            }
        }
        log.info(needed.toString());
        return needed;
    }

    @Override
    public void accept(String idReceiver, String idSender) {
        Person personReceiver = userRepositories.findByEmail(idReceiver);
        Person personSender = userRepositories.findByEmail(idSender);
        personReceiver.getFriends().add(personSender);
        personSender.getFriends().add(personReceiver);
        List<FriendRequest> friendRequests = friendRequestRepositories.findAll();
        for(FriendRequest friendRequest: friendRequests){
            if(friendRequest.getReceiver().equals(personReceiver.getEmail())){
                if(friendRequest.getSender().equals(personSender.getEmail())){
                    friendRequest.setState("ACCEPT");
                    break;
                }
            }
        }
    }

    @Override
    public void decline(String idReceiver, String idSender) {
        Person personReceiver = userRepositories.findByEmail(idReceiver);
        Person personSender = userRepositories.findByEmail(idSender);
        List<FriendRequest> friendRequests = friendRequestRepositories.findAll();
        for(FriendRequest friendRequest: friendRequests){
            if(friendRequest.getReceiver().equals(personReceiver.getEmail())){
                if(friendRequest.getSender().equals(personSender.getEmail())){
                    friendRequest.setState("DECLINE");
                    break;
                }
            }
        }
    }

    @Override
    public Post savePost(Post post) {
        log.info("Save new Post {} by User {}", post.getTitle(), post.getPerson().getEmail());
        return postRepositories.save(post);
    }

    @Override
    public Post getPost(Long id) {
        Optional<Post> post = postRepositories.findById(id);
        return post.isPresent() ? post.get() : new Post();
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepositories.findAll();
    }

    @Override
    public List<Post> getAllPostsByUser(Person person) {
        return postRepositories.findAllByPerson(person);
    }

    @Override
    public Comment saveComment(Comment comment) {
        log.info("Saving new Comment {} by Post {} by User {}", comment.getStr(),
                comment.getPost().getTitle(), comment.getPerson().getEmail());
        return commentRepositories.save(comment);
    }

    @Override
    public List<Comment> getAllCommentByPost(Post post) {
        return commentRepositories.findAllByPost(post);
    }

    @Override
    public Comment getComment(Long id) {
        Optional<Comment> comment = commentRepositories.findById(id);
        return comment.isPresent() ? comment.get() : new Comment();
    }
}
