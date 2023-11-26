package com.restapi.service.validation;

import com.restapi.model.request.RegisterUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Service
public class ValidationServiceBean implements ValidationService{
   @Autowired
   private Validator validator;
   public void validate(Object request) {
      Set<ConstraintViolation<Object>> constraintViolation = validator.validate(request);

      if (!constraintViolation.isEmpty()) {
         throw new ConstraintViolationException(constraintViolation);
      }
   }
}
