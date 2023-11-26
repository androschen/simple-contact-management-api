package com.restapi.service.authentication;

import com.restapi.model.request.LoginUserRequest;
import com.restapi.model.response.TokenResponse;

public interface AuthenticationService {
   TokenResponse login(LoginUserRequest request);
}
