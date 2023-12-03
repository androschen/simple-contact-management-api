package com.restapi.model.request.contact;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
public class UpdateContactRequest {
   @JsonIgnore
   @NotBlank
   private String id;
   @NotBlank
   private String firstName;
   private String lastName;

   @Size(max = 100)
   @Email
   private String email;

   @Size(max = 100)
   @Pattern(regexp = "^[0-9]{8,13}$", message = "phone must be number and 8-13 length")
   private String phone;
}
