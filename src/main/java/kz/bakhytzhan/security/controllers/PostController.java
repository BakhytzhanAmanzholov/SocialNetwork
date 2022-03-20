package kz.bakhytzhan.security.controllers;


import kz.bakhytzhan.security.services.UserImplementation;
import kz.bakhytzhan.security.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final UserImplementation userImplementation;
    private final UserService userService;

}
