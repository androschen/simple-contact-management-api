package com.restapi.controller.contact;

import com.restapi.entity.User;
import com.restapi.model.BaseResponse;
import com.restapi.model.request.CreateContactRequest;
import com.restapi.model.response.ContactResponse;
import com.restapi.service.contact.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ContactControllerPath.BASE_PATH)
public class ContactController {
   @Autowired
   private ContactService contactService;

   @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public BaseResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request) {
      return BaseResponse.<ContactResponse>builder()
              .data(contactService.create(user, request))
              .success(true)
              .build();
   }
}
