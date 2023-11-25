package com.restapi.controller.user;

import com.restapi.model.BaseResponse;
import com.restapi.model.request.RegisterUserRequest;
import com.restapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = UserControllerPath.BASE_PATH)
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = UserControllerPath.REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<String> register(@RequestBody RegisterUserRequest request) {
        userService.register(request);
        return BaseResponse.<String>builder().data("OK").success(true).build();
    }
}
