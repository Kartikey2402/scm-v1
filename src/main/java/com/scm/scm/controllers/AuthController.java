package com.scm.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.scm.entities.User;
import com.scm.scm.helper.Message;
import com.scm.scm.helper.MessageType;
import com.scm.scm.repositories.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    // verify email
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/verify-email")
    public String verifyEmail(
            @RequestParam("token") String token, HttpSession session) {

        User user = userRepo.findByEmailToken(token).orElse(null);

        if (user != null) {
            // user fetch hua hai toh process krna hai
            if (user.getEmailToken().equals(token)) {
                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepo.save(user);
                session.setAttribute("message", Message.builder()
                        .content("Email is verified. Now you can login.")
                        .type(MessageType.green)
                        .build());

                System.out.println("Message set in session: " + session.getAttribute("message"));

                return "login";
            }

            session.setAttribute("message", Message.builder()
                    .content("Email is not verified. Verification link is sent to your email.")
                    .type(MessageType.red)
                    .build());

            System.out.println("Message set in session: " + session.getAttribute("message"));

            return "login";

        }
        session.setAttribute("message", Message.builder()
                .content("Email is not verified. Verification link is sent to your email")
                .type(MessageType.red)
                .build());

        System.out.println("Message set in session: " + session.getAttribute("message"));

        return "login";
    }
}
