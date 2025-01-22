package com.scm.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;

import com.scm.scm.entities.User;
import com.scm.scm.forms.UserForm;
import com.scm.scm.helper.Message;
import com.scm.scm.helper.MessageType;
import com.scm.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;






@Controller
public class MyController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    

    // Home page
    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("name", "Kartikey singh");
        model.addAttribute("leetcode", "https://leetcode.com/u/kartikey732/");
        return "home";
    }

    // about route
    @RequestMapping("/about")
    public String aboutPage(Model model){
        model.addAttribute("isLogin", true);
        System.out.println("About page loading");
        return "about";
    }
    
    //services route
    @RequestMapping("/services")
    public String servicesPage(){
        System.out.println("Services page loading");
        return "services";
    }

    // contact page
    @GetMapping("/contact")
    public String contactPage(){
        System.out.println("Contact page loading");
        return "contact";
    }

    // this is showing login page
    
    // Login page
    
    @GetMapping("/login")
    public String loginPage(){
        System.out.println("Login page loading");
        return "login";
    }

    // this is registration view 

    //Register page
    @GetMapping("/register")
    public String registerPage(Model model){
        UserForm  userForm = new UserForm();
        // default data can also be given
        //userForm.setName("");
        //userForm.setAbout("");
        //userForm.setPhoneNumber("");
        model.addAttribute("userForm", userForm);
        
        return "register";
    }

    //processing register 
    @RequestMapping(value = "/do-register", method=RequestMethod.POST)
    
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session){
        System.out.println(userForm);
        // fetch the form data
        // validate form data...if error then don't proceed further
        if(rBindingResult.hasErrors()){
            return "register";
        }
        // else proceed the registration
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setProfilePic("/images/profilePic");


        User saveUser = userService.saveUser(user);
        System.out.println("Saved User Profile Pic: " + saveUser.getProfilePic());

        System.out.println("User saved");
        

        // message = registration successfull
        // Add the message
        Message message = Message.builder().content("Registration Successful. Please verify the email.").type(MessageType.green).build();
        session.setAttribute("message", message);
        // redirect

        return "redirect:/register";
    }

    
}
