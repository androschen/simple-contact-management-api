package com.restapi.service.user;

import com.restapi.entity.User;
import com.restapi.model.request.user.RegisterUserRequest;
import com.restapi.model.request.user.UpdateUserRequest;
import com.restapi.model.response.UserResponse;

public interface UserService {
    void register(RegisterUserRequest request);
    UserResponse get(User user);
    UserResponse update(User user, UpdateUserRequest request);
}
