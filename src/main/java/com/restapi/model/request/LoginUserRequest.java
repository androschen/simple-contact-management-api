package com.restapi.model.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class LoginUserRequest {
   @NotBlank
   @Size(max = 100)
   private String username;
   @NotBlank
   @Size(max = 100)
   private String password;
}
