package com.restapi.model.request.contact;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class SearchContactRequest {
   private String name;
   private String email;
   private String phone;
   @NotNull
   private Integer page;
   @NotNull
   private Integer size;
}
