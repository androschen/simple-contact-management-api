package com.restapi.controller.contact;

import com.restapi.entity.Address;
import com.restapi.entity.Contact;
import com.restapi.entity.User;
import com.restapi.model.BaseResponse;
import com.restapi.model.request.address.CreateAddressRequest;
import com.restapi.model.request.contact.CreateContactRequest;
import com.restapi.model.request.contact.SearchContactRequest;
import com.restapi.model.request.contact.UpdateContactRequest;
import com.restapi.model.response.AddressResponse;
import com.restapi.model.response.ContactResponse;
import com.restapi.model.response.PagingResponse;
import com.restapi.service.address.AddressService;
import com.restapi.service.contact.ContactService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = ContactControllerPath.BASE_PATH)
public class ContactController {
   @Autowired
   private ContactService contactService;

   @Autowired
   private AddressService addressService;

   @Autowired
   private ModelMapper modelMapper;

   @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public BaseResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request) {
      return BaseResponse.<ContactResponse>builder()
              .data(contactService.create(user, request))
              .success(true)
              .build();
   }

   @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
   public BaseResponse<List<ContactResponse>> get(User user,
                                                  @RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "email", required = false) String email,
                                                  @RequestParam(value = "phone", required = false) String phone,
                                                  @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
      SearchContactRequest request = SearchContactRequest.builder()
              .name(name)
              .email(email)
              .page(page)
              .phone(phone)
              .size(size)
              .build();

      Page<Contact> contactResponsePaging = contactService.search(user, request);
      List<ContactResponse> contactResponses = contactResponsePaging.getContent()
              .stream()
              .map(c -> modelMapper.map(c, ContactResponse.class))
              .collect(Collectors.toList());

      return BaseResponse.<List<ContactResponse>>builder()
              .data(contactResponses)
              .paging(PagingResponse.builder()
                      .currentPage(contactResponsePaging.getNumber())
                      .totalPage(contactResponsePaging.getTotalPages())
                      .size(contactResponsePaging.getSize())
                      .build())
              .success(true)
              .build();
   }

   @GetMapping(value = "/{contactId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public BaseResponse<ContactResponse> get(User user, @PathVariable("contactId") String contactId) {
      Contact contact = contactService.get(user, contactId);
      ContactResponse contactResponse = modelMapper.map(contact, ContactResponse.class);

      return BaseResponse.<ContactResponse>builder()
              .data(contactResponse)
              .success(true)
              .build();
   }

   @PutMapping(value = "/{contactId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public BaseResponse<ContactResponse> update(User user, @RequestBody UpdateContactRequest request,
                                               @PathVariable("contactId") String contactId) {
      request.setId(contactId);
      Contact contact = contactService.update(user, request);
      ContactResponse contactResponse = modelMapper.map(contact, ContactResponse.class);

      return BaseResponse.<ContactResponse>builder()
              .data(contactResponse)
              .success(true)
              .build();
   }

   @DeleteMapping(value = "/{contactId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public BaseResponse<Object> delete(User user, @PathVariable("contactId") String contactId) {
      contactService.delete(user, contactId);

      return BaseResponse.builder()
              .data("OK")
              .success(true)
              .build();
   }

   @PostMapping(value = "/{contactId}/addresses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public BaseResponse<AddressResponse> addAddress(User user, @RequestBody CreateAddressRequest request,
                                                   @PathVariable("contactId") String contactId) {
      request.setContactId(contactId);
      Address address = addressService.create(user, request);
      AddressResponse response = modelMapper.map(address, AddressResponse.class);

      return BaseResponse.<AddressResponse>builder()
              .data(response)
              .success(true)
              .build();
   }

   @GetMapping(value = "/{contactId}/addresses/{addressId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public BaseResponse<AddressResponse> getAddress(User user,
                                                   @PathVariable("contactId") String contactId,
                                                   @PathVariable("addressId") String addressId) {
      Address address = addressService.get(user, contactId, addressId);
      AddressResponse response = modelMapper.map(address, AddressResponse.class);

      return BaseResponse.<AddressResponse>builder()
              .data(response)
              .success(true)
              .build();
   }
}
