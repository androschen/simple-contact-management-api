package com.restapi.service.authentication;

import com.restapi.entity.User;
import com.restapi.model.request.user.LoginUserRequest;
import com.restapi.model.response.TokenResponse;

public interface AuthenticationService {
   TokenResponse login(LoginUserRequest request);
   void logout(User user);
}
