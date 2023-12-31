package com.restapi.repository;

import com.restapi.entity.Address;
import com.restapi.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
   Optional<Address> findFirstByContactAndId(Contact contact, String id);
}
