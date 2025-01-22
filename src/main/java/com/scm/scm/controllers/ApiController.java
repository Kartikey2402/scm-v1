package com.scm.scm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.scm.entities.Contacts;
import com.scm.scm.services.ContactServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api")
public class ApiController {
    // get contact 
    @Autowired
    private ContactServices contactServices;
    @GetMapping("/contacts/{contactId}")
    public Contacts getContacts(@PathVariable String contactId){
        return contactServices.getById(contactId);
    }
}


