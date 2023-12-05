package com.restapi.model.request.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class CreateAddressRequest {
   @Size(max = 200)
   private String street;
   @Size(max = 100)
   private String city;
   @Size(max = 100)
   private String province;
   @NotBlank
   @Size(max = 100)
   private String country;
   @Size(max = 10)
   private String postalCode;
   @JsonIgnore
   @NotBlank
   private String contactId;
}
