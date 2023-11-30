package com.restapi.service.contact;

import com.restapi.entity.Contact;
import com.restapi.entity.User;
import com.restapi.model.request.CreateContactRequest;
import com.restapi.model.response.ContactResponse;
import com.restapi.repository.ContactRepository;
import com.restapi.service.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ContactServiceBean implements ContactService{
   @Autowired
   private ContactRepository contactRepository;

   @Autowired
   private ValidationService validationService;

   public ContactResponse create(User user, CreateContactRequest request){
      validationService.validate(request);

      Contact contact = Contact.builder()
              .id(UUID.randomUUID().toString())
              .firstName(request.getFirstName())
              .lastName(request.getLastName())
              .phone(request.getPhone())
              .email(request.getEmail())
              .build();
      contactRepository.save(contact);

      return ContactResponse.builder()
              .id(contact.getId())
              .firstName(contact.getFirstName())
              .lastName(contact.getLastName())
              .email(contact.getEmail())
              .phone(contact.getPhone())
              .build();
   }


}
