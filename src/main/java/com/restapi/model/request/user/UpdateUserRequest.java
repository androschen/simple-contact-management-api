package com.restapi.model.request.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class UpdateUserRequest {
   @NotBlank
   @Size(max = 100)
   private String name;
   @NotBlank
   @Size(max = 100)
   private String password;
}
