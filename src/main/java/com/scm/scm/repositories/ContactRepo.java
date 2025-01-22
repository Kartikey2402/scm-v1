package com.scm.scm.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.scm.entities.Contacts;
import com.scm.scm.entities.User;

@Repository
public interface ContactRepo extends JpaRepository<Contacts, String> {

    // find the contacts by user
    //custom finder method
    Page <Contacts> findByUser(User user, Pageable pageable);

    //custom query method...we can use any one of them
    @Query("SELECT c FROM Contacts c WHERE c.user.id = :userId")
    List<Contacts> findByUserId(@Param("userId") String userId);

    Page<Contacts> findByUserAndNameContaining(User user,String nameKeyword, Pageable pageable);
    Page<Contacts> findByUserAndEmailContaining(User user,String emailKeyword, Pageable pageable);
    Page<Contacts> findByUserAndPhoneNumberContaining(User user,String phoneKeyword, Pageable pageable);
}
