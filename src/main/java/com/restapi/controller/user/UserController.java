package com.restapi.controller.user;

import com.restapi.entity.User;
import com.restapi.model.BaseResponse;
import com.restapi.model.request.RegisterUserRequest;
import com.restapi.model.request.UpdateUserRequest;
import com.restapi.model.response.UserResponse;
import com.restapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = UserControllerPath.BASE_PATH)
public class UserController {
   @Autowired
   private UserService userService;

   @PostMapping(value = UserControllerPath.REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public BaseResponse<String> register(@RequestBody RegisterUserRequest request) {
      userService.register(request);
      return BaseResponse.<String>builder()
              .data("OK")
              .success(true)
              .build();
   }

   @GetMapping(value = UserControllerPath.CURRENT, produces = MediaType.APPLICATION_JSON_VALUE)
   public BaseResponse<UserResponse> get(User user) {
      UserResponse userResponse = userService.get(user);
      return BaseResponse.<UserResponse>builder()
              .data(userResponse)
              .success(true)
              .build();
   }

   @PatchMapping(value = UserControllerPath.CURRENT, consumes = MediaType.APPLICATION_JSON_VALUE)
   public BaseResponse<UserResponse> update(User user, @RequestBody UpdateUserRequest request) {
      UserResponse userResponse = userService.update(user, request);
      return BaseResponse.<UserResponse>builder()
              .data(userResponse)
              .success(true)
              .build();
   }
}
