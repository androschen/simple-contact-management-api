package com.restapi.repository;

import com.restapi.entity.Contact;
import com.restapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
   List<Contact> findAllByUser(User user);
   Optional<Contact> findFirstByUserAndId(User user, String id);
}
