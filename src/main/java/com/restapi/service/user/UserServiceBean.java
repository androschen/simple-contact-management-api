package com.restapi.service.user;

import com.restapi.constant.ErrorMessages;
import com.restapi.entity.User;
import com.restapi.model.request.user.RegisterUserRequest;
import com.restapi.model.request.user.UpdateUserRequest;
import com.restapi.model.response.UserResponse;
import com.restapi.repository.UserRepository;
import com.restapi.service.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

import java.util.Objects;

@Service
public class UserServiceBean implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUserRequest request) {
        validationService.validate(request);

        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.ERR_USERNAME_IS_EXIST);
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
                .name(request.getName())
                .build();

        userRepository.save(user);
    }

    public UserResponse get(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request) {
        validationService.validate(request);

        if (Objects.nonNull(request.getName()))
            user.setName(request.getName());

        if(Objects.nonNull(request.getPassword()))
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));

        userRepository.save(user);

        return UserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .build();
    }
}
