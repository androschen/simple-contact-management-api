package com.restapi.service.contact;

import com.restapi.constant.ErrorMessages;
import com.restapi.entity.Contact;
import com.restapi.entity.User;
import com.restapi.model.request.contact.CreateContactRequest;
import com.restapi.model.request.contact.UpdateContactRequest;
import com.restapi.model.response.ContactResponse;
import com.restapi.repository.ContactRepository;
import com.restapi.service.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ContactServiceBean implements ContactService {
   @Autowired
   private ContactRepository contactRepository;

   @Autowired
   private ValidationService validationService;

   public ContactResponse create(User user, CreateContactRequest request) {
      validationService.validate(request);

      Contact contact = Contact.builder()
              .id(UUID.randomUUID()
                      .toString())
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

   @Transactional
   public List<Contact> getAll(User user) {
      return contactRepository.findAllByUser(user);
   }

   @Transactional
   public Contact get(User user, String contactId) {
      return contactRepository.findFirstByUserAndId(user, contactId)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                      ErrorMessages.ERR_CONTACT_NOT_FOUND
              ));
   }

   @Transactional
   public Contact update(User user, UpdateContactRequest request) {
      validationService.validate(request);

      Contact contact = contactRepository.findFirstByUserAndId(user, request.getId())
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                      ErrorMessages.ERR_CONTACT_NOT_FOUND
              ));

      if (Objects.nonNull(request.getEmail())) contact.setEmail(request.getEmail());

      if (Objects.nonNull(request.getFirstName())) contact.setFirstName(request.getFirstName());

      if (Objects.nonNull(request.getLastName())) contact.setLastName(request.getLastName());

      if (Objects.nonNull(request.getPhone())) contact.setPhone(request.getPhone());

      return contact;
   }

   @Transactional
   public void delete(User user, String contactId) {
      Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                      ErrorMessages.ERR_CONTACT_NOT_FOUND
              ));

      contactRepository.delete(contact);
   }

}
