package com.restapi.service.contact;

import com.restapi.entity.Contact;
import com.restapi.entity.User;
import com.restapi.model.request.contact.CreateContactRequest;
import com.restapi.model.request.contact.UpdateContactRequest;
import com.restapi.model.response.ContactResponse;

import java.util.List;

public interface ContactService {
   ContactResponse create(User user, CreateContactRequest request);
   List<Contact> getAll(User user);
   Contact get(User user, String id);
   Contact update(User user, UpdateContactRequest request);
   void delete(User user, String id);
}
