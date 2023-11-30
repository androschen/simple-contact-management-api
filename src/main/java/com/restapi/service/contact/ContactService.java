package com.restapi.service.contact;

import com.restapi.entity.User;
import com.restapi.model.request.CreateContactRequest;
import com.restapi.model.response.ContactResponse;

public interface ContactService {
   ContactResponse create(User user, CreateContactRequest request);
}
