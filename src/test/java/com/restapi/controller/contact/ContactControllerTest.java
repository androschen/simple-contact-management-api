package com.restapi.controller.contact;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapi.entity.User;
import com.restapi.model.BaseResponse;
import com.restapi.model.request.CreateContactRequest;
import com.restapi.model.response.ContactResponse;
import com.restapi.model.response.UserResponse;
import com.restapi.repository.ContactRepository;
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
public class ContactControllerTest {
   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private ContactRepository contactRepository;

   @BeforeEach
   void setUp() {
      contactRepository.deleteAll();
      userRepository.deleteAll();

      User user = User.builder()
              .username("TEST")
              .password(BCrypt.hashpw("TEST", BCrypt.gensalt()))
              .name("TEST")
              .token("TEST")
              .tokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 60))
              .build();
      userRepository.save(user);
   }

   @Test
   void createContactSuccess() throws Exception {
      CreateContactRequest request = CreateContactRequest.builder()
              .firstName("TEST")
              .lastName("TEST")
              .email("TEST@email.com")
              .phone("081234567")
              .build();

      mockMvc.perform(post("/api/contacts").accept(MediaType.APPLICATION_JSON)
                      .header("X-API-TOKEN", "TEST")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andExpectAll(status().isOk())
              .andDo(result -> {
                 BaseResponse<ContactResponse> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertTrue(response.isSuccess());
                 assertNull(response.getErrors());
                 assertEquals(request.getFirstName(), response.getData()
                         .getFirstName());
                 assertEquals(request.getEmail(), response.getData()
                         .getEmail());
                 assertEquals(request.getPhone(), response.getData()
                         .getPhone());
              });
   }

   @Test
   void createContactBadRequest() throws Exception {
      CreateContactRequest request = CreateContactRequest.builder()
              .firstName("TEST")
              .lastName("TEST")
              .phone("023123")
              .build();

      mockMvc.perform(post("/api/contacts").accept(MediaType.APPLICATION_JSON)
                      .header("X-API-TOKEN", "TEST")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andExpectAll(status().isBadRequest())
              .andDo(result -> {
                 BaseResponse<UserResponse> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertFalse(response.isSuccess());
                 assertNotNull(response.getErrors());
              });
   }
}
