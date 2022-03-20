package kz.bakhytzhan.security.controllers;

import kz.bakhytzhan.security.dao.UserDAO;
import kz.bakhytzhan.security.models.Person;
import kz.bakhytzhan.security.models.Role;
import kz.bakhytzhan.security.services.UserImplementation;
import kz.bakhytzhan.security.services.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final UserService userService;
    private final UserImplementation userImplementation;
    private final UserDAO userDAO;
    @GetMapping("/users")
    public ResponseEntity<List<Person>>getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }
    @GetMapping()
    public String index() {
        return "admin/index";
    }

    @GetMapping("/role/add")
    public String addRoleToUser(@ModelAttribute("form") FormFormRoleToUser form) {
        return "admin/role/add";
    }

    @PostMapping("/role")
    public String finalAddRoleToUser(@ModelAttribute("form") FormFormRoleToUser form){
        userService.addRoleToUser(form.getEmail(), form.getRoleName());
        return "redirect:/admin";
    }


}


@Data
class FormFormRoleToUser {
    private String email;
    private String roleName;

}
