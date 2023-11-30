package com.restapi.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactResponse {
   private String id;
   private String firstName;
   private String lastName;
   private String email;
   private String phone;
}
