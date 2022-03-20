package kz.bakhytzhan.security.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bakhytzhan.security.dao.UserDAO;
import kz.bakhytzhan.security.models.Person;
import kz.bakhytzhan.security.models.Post;
import kz.bakhytzhan.security.models.Role;
import kz.bakhytzhan.security.services.UserImplementation;
import kz.bakhytzhan.security.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final UserDAO userDAO;
    private final UserImplementation userImplementation;
    private final UserService userService;


    @GetMapping()
    public String index(Model model) {
        List<Person> people = userImplementation.getUsers();
        List<Person> neededUsers = new ArrayList<>();
        List<Post> posts = userImplementation.getAllPosts();
        List<Post> neededPosts = new ArrayList<>();
        if(isLogged().equals("anonymousUser")){
            model.addAttribute("email", null);
            model.addAttribute("person",null);
            for (Person person: people) {
                if(person.getState().equals("ALL") ){
                    neededUsers.add(person);
                }
            }
            for (Post post: posts) {
                if(post.getState().equals("ALL")){
                    neededPosts.add(post);
                }
            }
        }
        else{
            model.addAttribute("email", isLogged());
            model.addAttribute("person",userImplementation.getUser(isLogged()));
            Person personOwner = userImplementation.getUser(isLogged());
            for (Person person: people) {
                if(person!=personOwner){
                    if(person.getState().equals("LOGGED"))
                        neededUsers.add(person);
                    else if(person.getState().equals("FRIENDS") && personOwner.getFriends().contains(person))
                        neededUsers.add(person);
                    else if(person.getState().equals("ALL") )
                        neededUsers.add(person);
                }
            }
            for (Post post: posts) {
                if(post.getPerson()!=personOwner){
                    if(post.getState().equals("LOGGED"))
                        neededPosts.add(post);
                    else if(post.getState().equals("FRIENDS") && personOwner.getFriends().contains(post.getPerson()))
                        neededPosts.add(post);
                    else if(post.getState().equals("ALL") )
                        neededPosts.add(post);
                }
            }
        }
        model.addAttribute("users", neededUsers);
        model.addAttribute("posts", neededPosts);
        log.info(isLogged());
        return "index";
    }
    @GetMapping("/login")
    public String getLogin(Model model){
        return "login";
    }

    @GetMapping("/registration")
    public String registration (@ModelAttribute("person") Person person) {
        return "registration";
    }

    @PostMapping("/reg")
    public String finalRegistration(@ModelAttribute("person") @Valid Person person,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.saveUser(person);
        userService.addRoleToUser(person.getEmail(), "ROLE_USER");
        return "redirect:";
    }
    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request){
        String logoutEmail = request.getHeader("Logout");
        Person user = userService.getUser(logoutEmail);
        user.setToken(null);
        return ResponseEntity.ok("You successfully logged out!");
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

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        Cookie[] cookies = request.getCookies();
        String refresh_token = null;
        if (cookies!=null){
            for (Cookie c : cookies) {
                if (c.getName().equals("refresh_token"))
                {
                    refresh_token = c.getValue();
                }
            }
        }
        if(refresh_token!=null){
            try{
                Algorithm algorithm = Algorithm.HMAC256("somebestsecret".getBytes());
                System.out.println("passed");
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT =  verifier.verify(refresh_token);
                String email = decodedJWT.getSubject();
                Person user = userService.getUser(email);
                String access_token = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                Cookie acc_token = new Cookie("access_token",access_token );
                acc_token.setMaxAge(7 * 24 * 60 * 60);
                acc_token.setHttpOnly(true);
                Cookie ref_token = new Cookie("refresh_token",refresh_token );
                ref_token.setMaxAge(7 * 24 * 60 * 60);
                ref_token.setHttpOnly(true);
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception exception){
                response.setHeader("error", exception.getMessage());
                response.setStatus(403);
                //response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }else{
            throw new RuntimeException("Refresh token is missing");
        }
    }
    public boolean isLogged(Person user, String token){
        if (user.getToken() == null){
            return false;
        }
        System.out.println(user.getToken() + " " + token);
        return user.getToken().equals(token);
    }
}
