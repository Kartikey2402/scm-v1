package com.scm.scm.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.scm.entities.Contacts;
import com.scm.scm.entities.User;
import com.scm.scm.helper.ResourceNotFoundException;
import com.scm.scm.repositories.ContactRepo;
import com.scm.scm.services.ContactServices;

@Service
public class ContactServiceImpl implements ContactServices{

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contacts save(Contacts contacts) {
        String contactid = UUID.randomUUID().toString();
        contacts.setId(contactid);
        return contactRepo.save(contacts);
    }

    @Override
    public Contacts update(Contacts contacts) {
        
        var contactOld = contactRepo.findById(contacts.getId()).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
        contactOld.setName(contacts.getName());
        contactOld.setAddress(contacts.getAddress());
        contactOld.setEmail(contacts.getEmail());
        contactOld.setDescription(contacts.getDescription());
        contactOld.setFavourite(contacts.isFavourite());
        contactOld.setPhoneNumber(contacts.getPhoneNumber());
        contactOld.setLinkedInLink(contacts.getLinkedInLink());
        contactOld.setWebsiteLink(contacts.getWebsiteLink());
        contactOld.setPicture(contacts.getPicture());
        contactOld.setCloudinaryImagePublicId(contacts.getCloudinaryImagePublicId());
        

        return contactRepo.save(contactOld);
    }

    @Override
    public List<Contacts> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contacts getById(String id) {
        
        return contactRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
    }

    @Override
    public void delete(String id) {
        var contact = contactRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
        contactRepo.delete(contact);
        
    }

    

    @Override
    public List<Contacts> getByUserid(String userId) {
        
        return contactRepo.findByUserId(userId);
    }

    @Override
    public Page<Contacts> getByUser(User user,int page, int size,String sortBy, String direction) {

        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page,size,sort);
        return contactRepo.findByUser(user,pageable);
    }

    @Override
    public Page<Contacts> searchByName(String nameKeyword, int size, int page, String sortBy, String direction, User user) {
        
        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndNameContaining(user,nameKeyword, pageable);
    }

    @Override
    public Page<Contacts> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String direction, User user) {
        
        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndPhoneNumberContaining(user,phoneNumberKeyword, pageable);
    }

    @Override
    public Page<Contacts> searchByEmail(String emailKeyword, int size, int page, String sortBy, String direction, User user) {

        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndEmailContaining(user,emailKeyword, pageable);
        
    }

    
}
