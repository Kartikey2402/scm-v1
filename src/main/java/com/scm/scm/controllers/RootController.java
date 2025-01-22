package com.scm.scm.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.scm.entities.User;
import com.scm.scm.helper.Helper;
import com.scm.scm.services.UserService;

@ControllerAdvice
public class RootController {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    // ye yha use kiya hai poore project mein use krne ke liye

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {

        if(authentication == null){
            return ;
        }
        System.out.println("Adding logged in user information to the model");
        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User logged in:{} ", username);


        // database se user ko fetch kr skte hain
        User user = userService.getUserByEmail(username);
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        System.out.println(user.getProfilePic());
        model.addAttribute("loggedInUser", user);

    }
}



