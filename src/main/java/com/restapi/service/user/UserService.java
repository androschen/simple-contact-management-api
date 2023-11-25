package com.restapi.service.user;

import com.restapi.model.request.RegisterUserRequest;

public interface UserService {
    void register(RegisterUserRequest request);
}
