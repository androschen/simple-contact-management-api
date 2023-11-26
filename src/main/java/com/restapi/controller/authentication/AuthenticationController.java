package com.restapi.controller.authentication;

import com.restapi.model.BaseResponse;
import com.restapi.model.request.LoginUserRequest;
import com.restapi.model.response.TokenResponse;
import com.restapi.service.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = AuthenticationControllerPath.BASE_PATH)
public class AuthenticationController {
   @Autowired
   private AuthenticationService authenticationService;

   @PostMapping(path = AuthenticationControllerPath.LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public BaseResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
      return BaseResponse.<TokenResponse>builder()
              .data(authenticationService.login(request))
              .success(true)
              .build();
   }
}
