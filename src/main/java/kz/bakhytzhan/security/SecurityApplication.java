package kz.bakhytzhan.security;

import kz.bakhytzhan.security.models.*;
import kz.bakhytzhan.security.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner run(UserService userService){
        return args -> {

            userService.saveRole(new Role(null, "ROLE_USER"));
            userService.saveRole(new Role(null, "ROLE_MANAGER"));
            userService.saveRole(new Role(null, "ROLE_ADMIN"));
            userService.saveUser(new Person(
                    null, "Bakhytzhan", "Amanzholov", "amanzholovbakhytzhan@gmail.com",
                    "password23A", "password23A", null, new ArrayList<>(), new ArrayList<>(), "ALL"
            ));
            userService.addRoleToUser("amanzholovbakhytzhan@gmail.com", "ROLE_ADMIN");
            userService.addRoleToUser("amanzholovbakhytzhan@gmail.com", "ROLE_USER");
            userService.addRoleToUser("amanzholovbakhytzhan@gmail.com", "ROLE_MANAGER");
        };
    }

}
