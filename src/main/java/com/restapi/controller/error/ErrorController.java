package com.restapi.controller.error;

import com.restapi.model.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorController {

   @ExceptionHandler
   public ResponseEntity<BaseResponse<String>> constraintViolationException(ConstraintViolationException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(BaseResponse.<String>builder().errors(e.getMessage()).build());
   }

   @ExceptionHandler
   public ResponseEntity<BaseResponse<String>> responseStatusException(ResponseStatusException e) {
      return ResponseEntity.status(e.getStatus())
              .body(BaseResponse.<String>builder().errors(e.getReason()).build());
   }
}
