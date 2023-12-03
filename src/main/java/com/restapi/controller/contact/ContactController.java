package com.restapi.controller.contact;

import com.restapi.entity.Contact;
import com.restapi.entity.User;
import com.restapi.model.BaseResponse;
import com.restapi.model.request.contact.CreateContactRequest;
import com.restapi.model.request.contact.UpdateContactRequest;
import com.restapi.model.response.ContactResponse;
import com.restapi.service.contact.ContactService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
   private ModelMapper modelMapper;

   @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public BaseResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request) {
      return BaseResponse.<ContactResponse>builder()
              .data(contactService.create(user, request))
              .success(true)
              .build();
   }

   @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
   public BaseResponse<List<ContactResponse>> get(User user) {
      List<ContactResponse> contactResponses = contactService.getAll(user)
              .stream()
              .map(c -> modelMapper.map(c, ContactResponse.class))
              .collect(Collectors.toList());

      return BaseResponse.<List<ContactResponse>>builder()
              .data(contactResponses)
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
}
