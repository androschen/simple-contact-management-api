package com.restapi.controller.address;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapi.entity.Contact;
import com.restapi.entity.User;
import com.restapi.model.BaseResponse;
import com.restapi.model.request.address.CreateAddressRequest;
import com.restapi.model.response.AddressResponse;
import com.restapi.repository.AddressRepository;
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
public class AddressTest {
   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private ContactRepository contactRepository;

   @Autowired
   private AddressRepository addressRepository;

   @BeforeEach
   void setUp() {
      contactRepository.deleteAll();
      userRepository.deleteAll();
      addressRepository.deleteAll();

      User user = User.builder()
              .username("TEST")
              .password(BCrypt.hashpw("TEST", BCrypt.gensalt()))
              .name("TEST")
              .token("TEST")
              .tokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 60))
              .build();
      userRepository.save(user);

      Contact contact = Contact.builder()
              .user(user)
              .id("TEST")
              .firstName("TEST")
              .lastName("TEST")
              .email("TEST@email.com")
              .phone("081234567")
              .build();
      contactRepository.save(contact);
   }

   @Test
   void createContactAddressSuccess() throws Exception {
      CreateAddressRequest request = CreateAddressRequest.builder()
              .country("COUNTRY")
              .city("CITY")
              .province("PROVINCE")
              .street("STREET")
              .postalCode("POSTALCODE")
              .contactId("TEST")
              .build();

      mockMvc.perform(
                      post("/api/contacts/" + request.getContactId() + "/address").accept(MediaType.APPLICATION_JSON)
                              .header("X-API-TOKEN", "TEST")
                              .contentType(MediaType.APPLICATION_JSON)
                              .content(objectMapper.writeValueAsString(request)))
              .andExpectAll(status().isOk())
              .andDo(result -> {
                 BaseResponse<AddressResponse> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertTrue(response.isSuccess());
                 assertNull(response.getErrors());
                 assertEquals(request.getCountry(), response.getData()
                         .getCountry());
                 assertEquals(request.getProvince(), response.getData()
                         .getProvince());
                 assertEquals(request.getCity(), response.getData()
                         .getCity());
              });
   }

   @Test
   void createContactAddressNotFound() throws Exception {
      CreateAddressRequest request = CreateAddressRequest.builder()
              .country("COUNTRY")
              .city("CITY")
              .province("PROVINCE")
              .street("STREET")
              .postalCode("POSTALCODE")
              .contactId("WRONG")
              .build();

      mockMvc.perform(
                      post("/api/contacts/" + request.getContactId() + "/address").accept(MediaType.APPLICATION_JSON)
                              .header("X-API-TOKEN", "TEST")
                              .contentType(MediaType.APPLICATION_JSON)
                              .content(objectMapper.writeValueAsString(request)))
              .andExpectAll(status().isNotFound())
              .andDo(result -> {
                 BaseResponse<String> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertFalse(response.isSuccess());
                 assertNotNull(response.getErrors());
              });
   }

   @Test
   void createContactAddressBadRequest() throws Exception {
      CreateAddressRequest request = CreateAddressRequest.builder()
              .country("")
              .city("CITY")
              .province("PROVINCE")
              .street("STREET")
              .postalCode("POSTALCODE")
              .contactId("WRONG")
              .build();

      mockMvc.perform(
                      post("/api/contacts/" + request.getContactId() + "/address").accept(MediaType.APPLICATION_JSON)
                              .header("X-API-TOKEN", "TEST")
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
}
