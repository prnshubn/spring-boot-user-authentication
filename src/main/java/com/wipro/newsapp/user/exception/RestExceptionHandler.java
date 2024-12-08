package com.wipro.newsapp.user.exception;

import com.wipro.newsapp.user.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(InvalidEmailOrPasswordException.class)
    public ResponseEntity<ErrorResponse> userNotFoundExceptionHandler(
            InvalidEmailOrPasswordException invalidEmailOrPasswordException) {
        ErrorResponse errorResponse = new ErrorResponse(invalidEmailOrPasswordException.getMessage(),
                HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> userAllReadyExist(UserAlreadyExistsException userAlreadyExists) {
        ErrorResponse errorResponse = new ErrorResponse(userAlreadyExists.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userAllReadyExist(UserNotFoundException userNotFound) {
        ErrorResponse errorResponse = new ErrorResponse(userNotFound.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
