package com.restapi.controller.contact;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapi.entity.Contact;
import com.restapi.entity.User;
import com.restapi.model.BaseResponse;
import com.restapi.model.request.contact.CreateContactRequest;
import com.restapi.model.request.contact.UpdateContactRequest;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

   @Test
   void getAllContactsSuccess() throws Exception {
      User user = userRepository.findById("TEST").orElseThrow();

      Contact contact = Contact.builder()
              .user(user)
              .id(UUID.randomUUID().toString())
              .firstName("TEST")
              .lastName("TEST")
              .email("TEST@email.com")
              .phone("081234567")
              .build();
      contactRepository.save(contact);
      Contact contact2 = Contact.builder()
              .user(user)
              .id(UUID.randomUUID().toString())
              .firstName("TEST2")
              .lastName("TEST2")
              .email("TEST2@email.com")
              .phone("0812365672")
              .build();
      contactRepository.save(contact2);

      mockMvc.perform(get("/api/contacts").accept(MediaType.APPLICATION_JSON)
                      .header("X-API-TOKEN", "TEST"))
              .andExpectAll(status().isOk())
              .andDo(result -> {
                 BaseResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertTrue(response.isSuccess());
                 assertNull(response.getErrors());
                 assertNotNull(response.getData());
                 assertEquals(contact.getFirstName(), response.getData().get(0).getFirstName());
                 assertEquals(contact2.getFirstName(), response.getData().get(1).getFirstName());
              });
   }

   @Test
   void getAllContactsUnauthorized() throws Exception {
      Contact contact = Contact.builder()
              .id(UUID.randomUUID().toString())
              .firstName("TEST")
              .lastName("TEST")
              .email("TEST@email.com")
              .phone("081234567")
              .build();
      contactRepository.save(contact);
      Contact contact2 = Contact.builder()
              .id(UUID.randomUUID().toString())
              .firstName("TEST2")
              .lastName("TEST2")
              .email("TEST2@email.com")
              .phone("0812365672")
              .build();
      contactRepository.save(contact2);

      mockMvc.perform(get("/api/contacts").accept(MediaType.APPLICATION_JSON))
              .andExpectAll(status().isUnauthorized())
              .andDo(result -> {
                 BaseResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertFalse(response.isSuccess());
                 assertNotNull(response.getErrors());
              });
   }

   @Test
   void getContactSuccess() throws Exception {
      User user = userRepository.findById("TEST").orElseThrow();

      Contact contact = Contact.builder()
              .user(user)
              .id(UUID.randomUUID().toString())
              .firstName("TEST")
              .lastName("TEST")
              .email("TEST@email.com")
              .phone("081234567")
              .build();
      contactRepository.save(contact);

      mockMvc.perform(get("/api/contacts/" + contact.getId()).accept(MediaType.APPLICATION_JSON)
                      .header("X-API-TOKEN", "TEST"))
              .andExpectAll(status().isOk())
              .andDo(result -> {
                 BaseResponse<ContactResponse> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertTrue(response.isSuccess());
                 assertNull(response.getErrors());
                 assertEquals(contact.getFirstName(), response.getData().getFirstName());
                 assertEquals(contact.getLastName(), response.getData().getLastName());
                 assertEquals(contact.getPhone(), response.getData().getPhone());
                 assertEquals(contact.getEmail(), response.getData().getEmail());
              });
   }
   @Test
   void getContactNotFound() throws Exception {
      User user = userRepository.findById("TEST").orElseThrow();

      Contact contact = Contact.builder()
              .user(user)
              .id("TEST")
              .firstName("TEST")
              .lastName("TEST")
              .email("TEST@email.com")
              .phone("081234567")
              .build();
      contactRepository.save(contact);

      mockMvc.perform(get("/api/contacts/235132").accept(MediaType.APPLICATION_JSON)
                      .header("X-API-TOKEN", "TEST"))
              .andExpectAll(status().isNotFound())
              .andDo(result -> {
                 BaseResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertFalse(response.isSuccess());
                 assertNotNull(response.getErrors());
              });
   }

   @Test
   void updateContactsSuccess() throws Exception {
      User user = userRepository.findById("TEST").orElseThrow();

      Contact contact = Contact.builder()
              .user(user)
              .id(UUID.randomUUID().toString())
              .firstName("TEST")
              .lastName("TEST")
              .email("TEST@email.com")
              .phone("081234567")
              .build();
      contactRepository.save(contact);

      UpdateContactRequest request = UpdateContactRequest.builder()
              .email("TESTUPDATE@email.com")
              .firstName("UPDATE")
              .lastName("UPDATE")
              .build();

      mockMvc.perform(put("/api/contacts/" + contact.getId()).accept(MediaType.APPLICATION_JSON)
                      .header("X-API-TOKEN", "TEST")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsBytes(request)))
              .andExpectAll(status().isOk())
              .andDo(result -> {
                 BaseResponse<ContactResponse> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertTrue(response.isSuccess());
                 assertNull(response.getErrors());
                 assertNotNull(response.getData());
                 assertEquals(request.getEmail(), response.getData().getEmail());
                 assertEquals(request.getFirstName(), response.getData().getFirstName());
                 assertEquals(request.getLastName(), response.getData().getLastName());
              });
   }

   @Test
   void updateContactBadRequest() throws Exception {
      UpdateContactRequest request = UpdateContactRequest.builder()
              .email("TESTUPDATE@email.com")
              .firstName("")
              .lastName("UPDATE")
              .phone("0882378273")
              .build();

      mockMvc.perform(put("/api/contacts/235132").accept(MediaType.APPLICATION_JSON)
                      .header("X-API-TOKEN", "TEST")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsBytes(request)))
              .andExpectAll(status().isBadRequest())
              .andDo(result -> {
                 BaseResponse<ContactResponse> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertFalse(response.isSuccess());
                 assertNotNull(response.getErrors());
              });
   }

   @Test
   void deleteContactSuccess() throws Exception {
      User user = userRepository.findById("TEST").orElseThrow();

      Contact contact = Contact.builder()
              .user(user)
              .id(UUID.randomUUID().toString())
              .firstName("TEST")
              .lastName("TEST")
              .email("TEST@email.com")
              .phone("081234567")
              .build();
      contactRepository.save(contact);

      mockMvc.perform(delete("/api/contacts/" + contact.getId()).accept(MediaType.APPLICATION_JSON)
                      .header("X-API-TOKEN", "TEST"))
              .andExpectAll(status().isOk())
              .andDo(result -> {
                 BaseResponse<String> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertTrue(response.isSuccess());
                 assertNull(response.getErrors());
                 assertEquals("OK", response.getData());

                 Optional<Contact> contactInDb = contactRepository.findById(contact.getId());

                 assertEquals(Optional.empty(), contactInDb);
              });
   }

   @Test
   void deleteContactNotFound() throws Exception {
      User user = userRepository.findById("TEST").orElseThrow();

      Contact contact = Contact.builder()
              .user(user)
              .id("TEST")
              .firstName("TEST")
              .lastName("TEST")
              .email("TEST@email.com")
              .phone("081234567")
              .build();
      contactRepository.save(contact);

      mockMvc.perform(delete("/api/contacts/235132").accept(MediaType.APPLICATION_JSON)
                      .header("X-API-TOKEN", "TEST"))
              .andExpectAll(status().isNotFound())
              .andDo(result -> {
                 BaseResponse<String> response = objectMapper.readValue(result.getResponse()
                         .getContentAsString(), new TypeReference<>() {
                 });

                 assertFalse(response.isSuccess());
                 assertNotNull(response.getErrors());
              });
   }


}
