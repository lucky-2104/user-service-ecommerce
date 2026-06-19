package com.ecommerce.user_service.exception;


import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.user_service.dto.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<?> handlesUserAlreadyExists(UserAlreadyExistsException ex){
		
		
		ErrorResponse error = new ErrorResponse
				(
					ex.getMessage(),
					HttpStatus.CONFLICT.value(),
					LocalDateTime.now()
				);
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
		
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> handlesValidationErrors(BadCredentialsException ex){
		
		
		ErrorResponse error = new ErrorResponse(
				
				ex.getMessage(),
				HttpStatus.BAD_REQUEST.value(),
				LocalDateTime.now()
				
				);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handlesValidationErrors(MethodArgumentNotValidException ex){
		
		
		ErrorResponse error = new ErrorResponse(
				
				ex.getMessage(),
				HttpStatus.BAD_REQUEST.value(),
				LocalDateTime.now()
				
				);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handlesAccessDeniedException(AccessDeniedException ex){
		
		ErrorResponse error = new ErrorResponse(
				
				ex.getMessage(),
				HttpStatus.FORBIDDEN.value(),
				LocalDateTime.now()
				);
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
				
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handlesGenericException(Exception ex){
		
		
		ErrorResponse error = new ErrorResponse(
				
				"Unexpected Error Occured",
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				LocalDateTime.now()
				);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		
	}

}
