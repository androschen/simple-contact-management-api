package com.restapi.service.contact;

import com.restapi.constant.ErrorMessages;
import com.restapi.entity.Contact;
import com.restapi.entity.User;
import com.restapi.model.request.contact.CreateContactRequest;
import com.restapi.model.request.contact.SearchContactRequest;
import com.restapi.model.request.contact.UpdateContactRequest;
import com.restapi.model.response.ContactResponse;
import com.restapi.repository.ContactRepository;
import com.restapi.service.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
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

   @Transactional
   public Page<Contact> search(User user, SearchContactRequest request) {
      Specification<Contact> specification = (root, query, criteriaBuilder) -> {
         List<Predicate> predicates = new ArrayList<>();
         predicates.add(criteriaBuilder.equal(root.get("user"), user));
         if (Objects.nonNull(request.getName())) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.get("firstName"), "%" + request.getName() + "%"),
                    criteriaBuilder.like(root.get("lastName"), "%" + request.getName() + "%")
            ));
         }
         if (Objects.nonNull(request.getEmail())) {
            predicates.add(criteriaBuilder.like(root.get("email"), "%" + request.getEmail() + "%"));
         }
         if (Objects.nonNull(request.getPhone())) {
            predicates.add(criteriaBuilder.like(root.get("phone"), "%" + request.getPhone() + "%"));
         }
         return query.where(predicates.toArray(new Predicate[]{}))
                 .getRestriction();
      };

      Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
      return contactRepository.findAll(specification, pageable);
   }
}
