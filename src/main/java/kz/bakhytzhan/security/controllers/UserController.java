package kz.bakhytzhan.security.controllers;


import kz.bakhytzhan.security.dao.UserDAO;
import kz.bakhytzhan.security.models.*;
import kz.bakhytzhan.security.services.UserImplementation;
import kz.bakhytzhan.security.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserDAO userDAO;
    private final UserImplementation userImplementation;
    private final UserService userService;

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", userDAO.index());
        return "user/index";
    }

    @GetMapping("/{id}/{prime}")
    public String cabinet(@PathVariable("id") int id, Model model, @PathVariable("prime") Boolean prime) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        log.info(currentPrincipalName);
        Person findUser = userDAO.show(id);
        Person user = userService.getUser(findUser.getEmail());
        model.addAttribute("person", user);
        model.addAttribute("friends", userService.getFriendRequest(user.getEmail()));
        model.addAttribute("prime", prime);
        model.addAttribute("posts", userService.getAllPostsByUser(user));
        return "user/cabinet";
    }
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", userDAO.show(id));
        return "user/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "user/edit";
        userDAO.update(id, person);
        return "redirect:../";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userDAO.delete(id);
        return "redirect:../";
    }

    @GetMapping("/addFriend")
    public String addingFriend (@ModelAttribute("friend") FriendRequest friendRequest) {
        return "user/addFriends";
    }

    @PostMapping("/addingFriend")
    public String finalAddingFriend(@ModelAttribute("friend")  FriendRequest friendRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        log.info(currentPrincipalName);
        log.info("hello");
        log.info(friendRequest.getReceiver());
        userService.addRequestFriend(currentPrincipalName, friendRequest.getReceiver());
        return "redirect:../";
    }
    @GetMapping("/newFriend/{emailSender}/{emailReceiver}")
    public String addFriend(@PathVariable ("emailSender") String emailSender, @PathVariable("emailReceiver") String emailReceiver){
        userImplementation.addRequestFriend(emailSender, emailReceiver);
        log.info(emailReceiver);
        return "redirect:../../../../";
    }
    @GetMapping("/accept/{idReceiver}/{idSender}")
    public String accept(@PathVariable("idReceiver") String idReceiver ,@PathVariable("idSender") String idSender){
        userImplementation.accept(idReceiver, idSender);
        return "redirect:../../../";
    }
    @GetMapping("/decline/{idReceiver}/{idSender}")
    public String decline(@PathVariable("idReceiver") String idReceiver ,@PathVariable("idSender") String idSender){
        userImplementation.decline(idReceiver, idSender);
        return "redirect:../../../";
    }
    public String isLogged(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        log.info(currentPrincipalName);
        if(!currentPrincipalName.equals("anonymousUser")){
            return currentPrincipalName;
        }
        return "anonymousUser";
    }

    @GetMapping("/cabinet/{emailUser}")
    public String ifHisCabinet(@PathVariable("emailUser") String emailUser ){
        Person person = userService.getUser(emailUser);
        String emailOwner = isLogged();
        String idPerson = String.valueOf(person.getId());
        boolean prime = false;
        if(emailOwner.equals(person.getEmail())){
           prime = true;
        }
        return "user/" + idPerson + "/" + prime;
    }

    @GetMapping("/newpost")
    public String creatingPost (@ModelAttribute("post") Post post) {
        return "posts/newpost";
    }


    @PostMapping("/createPost")
    public String createPost(@ModelAttribute("post") Post post) {
        Person person = userService.getUser(isLogged());
        post.setPerson(person);
        userService.savePost(post);
        return "redirect:../";
    }

    @GetMapping("/post/{id}/{prime}")
    public String post(@PathVariable("id") Long id, Model model, @PathVariable("prime") Boolean prime) {
        Post post = userService.getPost(id);
        List<Comment> comments = userService.getAllCommentByPost(post);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("prime", comments);
        System.out.println(post.getToday());
        System.out.println(post.getSub());
        return "user/post";
    }

    @GetMapping("/newcomments/{id}")
    public String creatingComment (@ModelAttribute("comment") Comment comment, @PathVariable("id") Long id,  Model model) {
        model.addAttribute("id", id);
        return "comments/newcomments";
    }


    @PostMapping("/createComment/{id}")
    public String createComment(@ModelAttribute("comment") Comment comment,  Model model, @PathVariable("id") Long id) {
        Person person = userService.getUser(isLogged());
        comment.setPerson(person);
        Post post = userService.getPost(id);
        comment.setPost(post);
        userService.saveComment(comment);
        return "redirect:../../";
    }
}