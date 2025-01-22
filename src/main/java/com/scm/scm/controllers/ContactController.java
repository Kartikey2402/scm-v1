package com.scm.scm.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.scm.entities.Contacts;
import com.scm.scm.entities.User;
import com.scm.scm.forms.ContactForm;
import com.scm.scm.forms.ContactSearchForm;
import com.scm.scm.helper.AppConstants;
import com.scm.scm.helper.Helper;
import com.scm.scm.helper.Message;
import com.scm.scm.helper.MessageType;
import com.scm.scm.services.ContactServices;
import com.scm.scm.services.ImageService;
import com.scm.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/user/contact")
public class ContactController {

    private Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactServices contactService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    // add contact page: handler
    @RequestMapping("/add")
    public String addContactView(Model model) {

        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/addContact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
            Authentication authentication, HttpSession session) {

        // process the form data

        // validate form
        // this is validation logic

        if (result.hasErrors()) {

            result.getAllErrors().forEach(error -> logger.info(error.toString()));
            session.setAttribute("message", Message.builder()
                    .content("Please correct the following errors")
                    .type(MessageType.red)
                    .build());
            return "user/addContact";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);

        // form -> contact
        User user = userService.getUserByEmail(username);

        Contacts contacts = new Contacts();
        contacts.setName(contactForm.getName());
        contacts.setFavourite(contactForm.isFavourite());
        contacts.setEmail(contactForm.getEmail());
        contacts.setAddress(contactForm.getAddress());
        contacts.setDescription(contactForm.getDescription());
        contacts.setPhoneNumber(contactForm.getPhoneNumber());
        contacts.setUser(user);
        contacts.setLinkedInLink(contactForm.getLinkedInLink());
        contacts.setWebsiteLink(contactForm.getWebsiteLink());

        //processing the image
        if((contactForm.getContactImage() != null) && (!contactForm.getContactImage().isEmpty())){
            String filename = UUID.randomUUID().toString();
            String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);
            contacts.setPicture(fileURL);
            contacts.setCloudinaryImagePublicId(filename);
        }
        contactService.save(contacts);
        System.out.println(contactForm);

        // SET THE CONTACT PICTURE URL

        // SET THE MESSAGE FOR DISPLAY
        session.setAttribute("message", Message.builder()
                .content("You have successfully added a new contact")
                .type(MessageType.green)
                .build());
        return "redirect:/user/contact/add";
    }

    // view contacts
    @RequestMapping("/contacts")
    public String viewContacts(

            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,
            Authentication authentication) {

        String username = Helper.getEmailOfLoggedInUser(authentication);

        var user = userService.getUserByEmail(username);
        logger.info("logged in user:{}", user);
        Page<Contacts> pageContact = contactService.getByUser(user, page, size, sortBy, direction);
        logger.info("fetched contacts:{} ", pageContact);
        model.addAttribute("page", page);
        model.addAttribute("size", AppConstants.PAGE_SIZE);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "user/contacts";
    }

    // Search handler

    @RequestMapping("/search")
    public String searchHandler(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,
            Authentication authentication) {
        logger.info("field {} keyword {}", contactSearchForm.getField(), contactSearchForm.getValue());
        if (contactSearchForm.getField() == null) {
            contactSearchForm.setField("");
        }
        if (contactSearchForm.getValue() == null) {
            contactSearchForm.setValue("");
        }
        User user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contacts> pageContact = null;
        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy,
                    direction, user);
        }

        logger.info("pageContact {}", pageContact);

        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("size", AppConstants.PAGE_SIZE);
        return "user/search";
    }

    // delete contact

    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable String contactId, HttpSession session) {
        logger.info("Attempting to delete contact with ID: {}", contactId);

        try {
            contactService.delete(contactId);
            logger.info("Contact with ID {} deleted successfully.", contactId);
            session.setAttribute("message", Message.builder()
            .content("Contact is deleted successfully!")
            .type(MessageType.green)
            .build());
        } catch (Exception e) {
            logger.error("Error deleting contact with ID {}: {}", contactId, e.getMessage());
        }

        return "redirect:/user/contact/contacts";
    }

    // update contact 
    @GetMapping("/view/{contactId}")
    public String updateContactFormView(@PathVariable("contactId") String contactId, Model model){
        var contact = contactService.getById(contactId);

        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setEmail(contact.getEmail());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavourite(contact.isFavourite());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setPicture(contact.getPicture());
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);
        return "user/update_contact_view";
    }

    // update contact

    @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
    public String updateContact(@PathVariable("contactId") String contactId, @Valid @ModelAttribute ContactForm contactForm,BindingResult bindingResult, Model model){

        //update the contact
        if(bindingResult.hasErrors()){
            return "user/update_contact_view";
        }
        var con = contactService.getById(contactId);
        con.setId(contactId);
        con.setName(contactForm.getName());
        con.setEmail(contactForm.getEmail());
        con.setAddress(contactForm.getAddress());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setDescription(contactForm.getDescription());
        con.setFavourite(contactForm.isFavourite());
        con.setLinkedInLink(contactForm.getLinkedInLink());
        con.setWebsiteLink(contactForm.getWebsiteLink());

        // process image
        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
            String fileName = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
            con.setCloudinaryImagePublicId(fileName);
            con.setPicture(imageUrl);
            contactForm.setPicture(imageUrl);
        }


        var updatedCon = contactService.update(con);
        logger.info("Updated contact {}", updatedCon);
        model.addAttribute("message", Message.builder()
        .content("Contact Updated Successfully")
        .type(MessageType.green)
        .build());
        return "redirect:/user/contact/contacts";
    }
}
