package com.ecommerce.user_service.service;



import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.user_service.dto.request.LoginRequest;
import com.ecommerce.user_service.dto.request.RegisterRequest;
import com.ecommerce.user_service.dto.response.AuthResponse;
import com.ecommerce.user_service.entity.Role;
import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.exception.UserAlreadyExistsException;

import com.ecommerce.user_service.repository.UserRepository;
import com.ecommerce.user_service.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	

	
	public AuthResponse register(RegisterRequest request) {
		
		//Check if user already exist;
		boolean isUserPresent = userRepository.findByEmail(request.getEmail()).isPresent();
		if(isUserPresent) throw new UserAlreadyExistsException("User with email ID already Exist");
		
		//Hashing of password using the passwordEncoder.
		String hashedPasswordFromRequest = passwordEncoder.encode(request.getPassword());
		
		
		//Creating user with hashedPassword
		User user = User.builder()
				.email(request.getEmail())
				.password(hashedPasswordFromRequest)
				.fullName(request.getFullName())
				.role(Role.CUSTOMER)
				.build();
		
		//Saving the user

		userRepository.save(user);
		
		//Generating token
		String accessToken = jwtService.generateAccessToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);
		
		return AuthResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.expiresIn(900000L)
				.build();
	}
	
	public AuthResponse login(LoginRequest request) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
				request.getEmail(),
				request.getPassword()
				)
			);
		User user =  (User) authentication.getPrincipal();     
		
		//Remember — UserDetailsService returns your User object (since User implements UserDetails). 
		// The Authentication object stores that as its principal.
		//This saves a redundant database round trip — small but matters at scale.
		
		String accessToken = jwtService.generateAccessToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);
		
		
		
		
		
		return AuthResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.expiresIn(900000L)
				.build();
	}

}
