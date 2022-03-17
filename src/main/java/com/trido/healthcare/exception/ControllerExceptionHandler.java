package com.trido.healthcare.exception;

import com.trido.healthcare.constants.ConstantMessages;
import com.trido.healthcare.controller.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> invalidRequestException(InvalidRequestException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getTimestamp(), e.getError(), e.getError_description(), e.getHttpStatus());
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

    @ExceptionHandler(StorageNotFoundException.class)
    public ResponseEntity<?> invalidRequestException(StorageNotFoundException e) {
        ErrorResponse errorResponse =
                new ErrorResponse(LocalDateTime.now(), ConstantMessages.INTERNAL_SERVER_ERROR, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
