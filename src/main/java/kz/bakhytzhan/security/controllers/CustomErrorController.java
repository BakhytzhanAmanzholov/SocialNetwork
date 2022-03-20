package kz.bakhytzhan.security.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController {

   /* private static final Logger LOGGER = LoggerFactory.getLogger(CustomErrorController.class);
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        String pageTitle = "Error";
        String errorPage = "error";
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                pageTitle = "Page Not Found";
                errorPage = "error/404";
                LOGGER.error("Error 404");
                return "/main/resources/templates/error/404.html";
            }
            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                pageTitle = "Access Denied";
                errorPage = "error/403";
                LOGGER.error("Error 403");
                return "/main/resources/templates/error/403.html";
            }
        }

        model.addAttribute("pageTitle", pageTitle);

        return "error";
    }*/

}
