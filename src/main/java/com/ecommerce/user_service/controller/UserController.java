package com.ecommerce.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class UserController {

	@GetMapping("/test")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> adminAccessOnlyEndpoint(){
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Accepted");
	}
}
