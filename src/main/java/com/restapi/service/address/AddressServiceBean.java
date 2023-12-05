package com.restapi.service.address;

import com.restapi.constant.ErrorMessages;
import com.restapi.entity.Address;
import com.restapi.entity.Contact;
import com.restapi.entity.User;
import com.restapi.model.request.address.CreateAddressRequest;
import com.restapi.repository.AddressRepository;
import com.restapi.repository.ContactRepository;
import com.restapi.service.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class AddressServiceBean implements AddressService {
   @Autowired
   private ValidationService validationService;

   @Autowired
   private ContactRepository contactRepository;

   @Autowired
   private AddressRepository addressRepository;

   @Transactional
   public Address create(User user, CreateAddressRequest request) {
      validationService.validate(request);

      Contact contact = contactRepository.findFirstByUserAndId(user, request.getContactId())
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.ERR_CONTACT_NOT_FOUND));

      Address address = Address.builder()
              .id(UUID.randomUUID().toString())
              .country(request.getCountry())
              .city(request.getCity())
              .province(request.getProvince())
              .street(request.getStreet())
              .postalCode(request.getPostalCode())
              .build();

      addressRepository.save(address);

      return address;
   }

   @Transactional
   public Address get(User user, String contactId, String addressId) {
      Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.ERR_CONTACT_NOT_FOUND));

      return addressRepository.findFirstByContactAndId(contact, addressId)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.ERR_ADDRESS_NOT_FOUND));
   }
}
