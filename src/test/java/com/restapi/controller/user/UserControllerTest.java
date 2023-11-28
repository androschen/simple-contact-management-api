package com.restapi.controller.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapi.entity.User;
import com.restapi.model.BaseResponse;
import com.restapi.model.request.RegisterUserRequest;
import com.restapi.model.request.UpdateUserRequest;
import com.restapi.model.response.UserResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
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
   void registerSuccess() throws Exception {
      RegisterUserRequest request = RegisterUserRequest.builder()
              .username("TEST")
              .password("TEST")
              .name("TEST")
              .build();


      mockMvc.perform(post("/api/users/register").accept(MediaType.APPLICATION_JSON)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andExpectAll(status().isOk())
              .andDo(result -> {
                 BaseResponse<String> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertTrue(response.isSuccess());
              });
   }

   @Test
   void registerBadRequest() throws Exception {
      RegisterUserRequest request = RegisterUserRequest.builder()
              .username("")
              .password("TEST")
              .name("")
              .build();


      mockMvc.perform(post("/api/users/register").accept(MediaType.APPLICATION_JSON)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andExpectAll(status().isBadRequest())
              .andDo(result -> {
                 BaseResponse<String> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertFalse(response.isSuccess());
                 assertNotNull(response.getErrors());
              });
   }

   @Test
   void registerDuplicate() throws Exception {
      User user = User.builder()
              .username("TEST")
              .password(BCrypt.hashpw("TEST", BCrypt.gensalt()))
              .name("TEST")
              .build();
      userRepository.save(user);

      RegisterUserRequest request = RegisterUserRequest.builder()
              .username("TEST")
              .password("TEST")
              .name("TEST")
              .build();

      mockMvc.perform(post("/api/users/register").accept(MediaType.APPLICATION_JSON)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andExpectAll(status().isBadRequest())
              .andDo(result -> {
                 BaseResponse<String> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertFalse(response.isSuccess());
                 assertNotNull(response.getErrors());
              });
   }

   @Test
   void userAuthorizedTokenSuccess() throws Exception {
      User user = User.builder()
              .username("TEST")
              .password(BCrypt.hashpw("TEST", BCrypt.gensalt()))
              .name("TEST")
              .token("TEST")
              .tokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 60))
              .build();
      userRepository.save(user);

      mockMvc.perform(get("/api/users/current").accept(MediaType.APPLICATION_JSON)
                      .header("X-API-TOKEN", user.getToken()))
              .andExpectAll(status().isOk())
              .andDo(result -> {
                 BaseResponse<UserResponse> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertTrue(response.isSuccess());
                 assertNull(response.getErrors());
                 assertNotNull(response.getData()
                         .getName());
                 assertNotNull(response.getData()
                         .getUsername());
              });
   }

   @Test
   void userUnauthorizedInvalidToken() throws Exception {
      mockMvc.perform(get("/api/users/current").accept(MediaType.APPLICATION_JSON)
                      .header("X-API-TOKEN", "InvalidToken-XYZ"))
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
   void userUnauthorizedWithoutToken() throws Exception {
      mockMvc.perform(get("/api/users/current").accept(MediaType.APPLICATION_JSON))
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
   void userAuthorizedTokenExpired() throws Exception {
      User user = User.builder()
              .username("TEST")
              .password(BCrypt.hashpw("TEST", BCrypt.gensalt()))
              .name("TEST")
              .token("TEST")
              .tokenExpiredAt(System.currentTimeMillis() - (1000 * 60 * 60))
              .build();
      userRepository.save(user);

      mockMvc.perform(get("/api/users/current").accept(MediaType.APPLICATION_JSON))
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
   void userUpdateSuccess() throws Exception {
      User user = User.builder()
              .username("TEST")
              .password(BCrypt.hashpw("TEST", BCrypt.gensalt()))
              .name("TEST")
              .token("TEST")
              .tokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 60))
              .build();
      userRepository.save(user);

      UpdateUserRequest request = UpdateUserRequest.builder()
              .name("TEST UPDATE")
              .password("TEST UPDATE")
              .build();

      mockMvc.perform(patch("/api/users/current").accept(MediaType.APPLICATION_JSON)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request))
                      .header("X-API-TOKEN", user.getToken()))
              .andExpectAll(status().isOk())
              .andDo(result -> {
                 BaseResponse<UserResponse> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertTrue(response.isSuccess());
                 assertNull(response.getErrors());
                 assertEquals(user.getUsername(), response.getData()
                         .getUsername());
                 assertEquals(request.getName(), response.getData()
                         .getName());

                 User userDB = userRepository.findById(user.getUsername()).orElse(null);
                 assertNotNull(userDB);
                 assertTrue(BCrypt.checkpw("TEST UPDATE", userDB.getPassword()));
              });
   }

   @Test
   void userUpdateUnauthorizedWithoutToken() throws Exception {
      UpdateUserRequest request = UpdateUserRequest.builder()
              .name("TEST")
              .password("TEST")
              .build();

      mockMvc.perform(patch("/api/users/current").accept(MediaType.APPLICATION_JSON)
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