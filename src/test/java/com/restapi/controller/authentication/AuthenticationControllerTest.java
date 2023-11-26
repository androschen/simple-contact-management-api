package com.restapi.controller.authentication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapi.entity.User;
import com.restapi.model.BaseResponse;
import com.restapi.model.request.LoginUserRequest;
import com.restapi.model.response.TokenResponse;
import com.restapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private UserRepository userRepository;

   @BeforeEach
   void resetTest() {
      userRepository.deleteAll();
   }

   @Test
   void loginSuccess() throws Exception {
      User user = User.builder()
              .name("TEST")
              .username("TEST")
              .password(BCrypt.hashpw("TEST", BCrypt.gensalt()))
              .build();
      userRepository.save(user);

      LoginUserRequest request = LoginUserRequest.builder()
              .username("TEST")
              .password("TEST")
              .build();

      mockMvc.perform(post("/api/auth/login").accept(MediaType.APPLICATION_JSON)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andExpectAll(status().isOk())
              .andDo(result -> {
                 BaseResponse<TokenResponse> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertTrue(response.isSuccess());
                 assertNotNull(response.getData().getToken());
                 assertNotNull(response.getData().getExpiredAt());
                 assertNull(response.getErrors());

                 User userInDb = userRepository.findById("TEST").orElse(null);
                 assertNotNull(userInDb);
                 assertEquals(userInDb.getToken(), response.getData().getToken());
                 assertEquals(userInDb.getTokenExpiredAt(), response.getData().getExpiredAt());
              });
   }

   @Test
   void loginFailedUserNotFound() throws Exception {
      LoginUserRequest request = LoginUserRequest.builder()
              .username("TEST")
              .password("TEST")
              .build();

      mockMvc.perform(post("/api/auth/login").accept(MediaType.APPLICATION_JSON)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andExpectAll(status().isUnauthorized())
              .andDo(result -> {
                 BaseResponse<String> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertFalse(response.isSuccess());
                 assertNotNull(response.getErrors());
              });
   }

   @Test
   void loginFailedWrongPassword() throws Exception {
      User user = User.builder()
              .name("TEST")
              .username("TEST")
              .password(BCrypt.hashpw("TEST", BCrypt.gensalt()))
              .build();
      userRepository.save(user);

      LoginUserRequest request = LoginUserRequest.builder()
              .username("TEST")
              .password("WRONG")
              .build();

      mockMvc.perform(post("/api/auth/login").accept(MediaType.APPLICATION_JSON)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andExpectAll(status().isUnauthorized())
              .andDo(result -> {
                 BaseResponse<String> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertFalse(response.isSuccess());
                 assertNotNull(response.getErrors());
              });
   }
}
