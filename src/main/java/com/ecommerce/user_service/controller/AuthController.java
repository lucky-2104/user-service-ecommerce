package com.ecommerce.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.user_service.dto.request.LoginRequest;
import com.ecommerce.user_service.dto.request.RegisterRequest;
import com.ecommerce.user_service.dto.response.AuthResponse;
import com.ecommerce.user_service.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	
	private final AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<AuthResponse>  registerUser(@RequestBody @Valid RegisterRequest request){
		
		
		AuthResponse authResponse = authService.register(request);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> loginUser(@RequestBody @Valid LoginRequest request)
	{
		AuthResponse authResponse = authService.login(request);
		return ResponseEntity.ok(authResponse);
	}
}
