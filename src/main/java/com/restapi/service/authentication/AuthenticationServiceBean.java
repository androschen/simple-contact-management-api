package com.restapi.service.authentication;

import com.restapi.entity.User;
import com.restapi.model.request.user.LoginUserRequest;
import com.restapi.model.response.TokenResponse;
import com.restapi.repository.UserRepository;
import com.restapi.service.validation.ValidationService;
import com.restapi.utils.TimeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class AuthenticationServiceBean implements AuthenticationService {
   @Autowired
   private ValidationService validationService;

   @Autowired
   private UserRepository userRepository;

   @Transactional
   public TokenResponse login(LoginUserRequest request) {
      validationService.validate(request);

      User user = userRepository.findById(request.getUsername())
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                      "Username or password wrong"
              ));

      if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
         user.setToken(UUID.randomUUID().toString());
         user.setTokenExpiredAt(TimeHelper.next30Days());
         return TokenResponse.builder()
                 .token(user.getToken())
                 .expiredAt(user.getTokenExpiredAt())
                 .build();
      }

      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
   }

   @Transactional
   public void logout(User user) {
      user.setToken(null);
      user.setTokenExpiredAt(null);

      userRepository.save(user);
   }
}
