package com.scm.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scm.scm.entities.Contacts;
import com.scm.scm.entities.User;

public interface ContactServices {

    //save contacts
    Contacts save(Contacts contacts);

    //update contacts
    Contacts update(Contacts contacts);

    //get contacts
    List<Contacts> getAll();

    //get contact by id
    Contacts getById(String id);

    // delete contact
    void delete(String id);

    //search contact
    Page<Contacts> searchByName(String nameKeyword, int size, int page, String sortBy, String direction, User user);
    Page<Contacts> searchByPhoneNumber(String phoneNumber,int size, int page, String sortBy, String direction, User user);
    Page<Contacts> searchByEmail(String email,int size, int page, String sortBy, String direction, User user);

    //get contacts by userid
    List<Contacts> getByUserid(String userid);

    Page <Contacts> getByUser(User user,int page, int size, String sortField, String sortDirection);
}
