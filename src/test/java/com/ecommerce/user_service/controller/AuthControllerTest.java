package com.ecommerce.user_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import com.ecommerce.user_service.config.SecurityConfig;
import com.ecommerce.user_service.dto.request.LoginRequest;
import com.ecommerce.user_service.dto.request.RegisterRequest;
import com.ecommerce.user_service.dto.response.AuthResponse;
import com.ecommerce.user_service.exception.UserAlreadyExistsException;
import com.ecommerce.user_service.security.JwtService;
import com.ecommerce.user_service.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	 @MockBean
	   private JwtService jwtService; 
	@MockBean
	private AuthService authService;
	
	@Test
	void register_shouldReturn201_whenRegistrationIsComplete() throws Exception {

	    String email = "lokeshjoshi@gmail.com";
	    String fullName = "Lokesh Joshi";
	    String normalPassword = "normalPassword";

	    RegisterRequest request = RegisterRequest.builder()
	            .email(email)
	            .fullName(fullName)
	            .password(normalPassword)
	            .build();

	    AuthResponse response = AuthResponse.builder()
	            .accessToken("fake-access-token")
	            .refreshToken("fake-refresh-token")
	            .tokenType("Bearer")
	            .build();

	    // ARRANGE
	    when(authService.register(any(RegisterRequest.class))).thenReturn(response);

	    // ACT + ASSERT
	    mockMvc.perform(post("/api/v1/auth/register")
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .content(objectMapper.writeValueAsString(request)))
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$.accessToken").value("fake-access-token"))
	            .andExpect(jsonPath("$.refreshToken").value("fake-refresh-token"))
	            .andExpect(jsonPath("$.tokenType").value("Bearer"));
	
		
		
	}
	@Test
	void register_shouldReturn400_whenValidationFails() throws Exception {
		
		String wrongEmail = "lokeshjoshi"; // not valid email format
	    String wrongFullName = ""; //Blank user name
	    String wrongNormalPassword = "ss"; // short password

	    RegisterRequest request = RegisterRequest.builder()
	            .email(wrongEmail)
	            .fullName(wrongFullName)
	            .password(wrongNormalPassword)
	            .build();

	    // Act + assert
	    
	    mockMvc.perform(
	    		post("/api/v1/auth/register")
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(objectMapper.writeValueAsString(request))
	    		)
	    .andExpect(status().isBadRequest())
	    .andExpect(jsonPath("$.email").value("Invalid email format"))
        .andExpect(jsonPath("$.fullName").value("Full name is required"))
        .andExpect(jsonPath("$.password").value("Password must be between 6 and 20 characters"));

		
		
	}
	
	@Test
	public void register_shouldReturn409_whenUserWithEmailAlreadyExist() throws  Exception {
		
		//Arrange
		String email = "lokeshjoshi@gmail.com"; // not valid email format
	    String fullName = "lokeshJoshi"; //Blank user name
	    String normalPassword = "normalPassword"; // short password

	    RegisterRequest request = RegisterRequest.builder()
	            .email(email)
	            .fullName(fullName)
	            .password(normalPassword)
	            .build();
	    
	    
	    //Act
	    
	    when(authService.register(any(RegisterRequest.class))).thenThrow(new UserAlreadyExistsException("User with email ID already Exist"));
	    
	    
	    //Assert
	    
	    mockMvc.perform(
	    		post("/api/v1/auth/register")
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(objectMapper.writeValueAsString(request))
	    		)
	    .andExpect(status().isConflict())
	    .andExpect(jsonPath("$.message").value("User with email ID already Exist"))
	    .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
	    .andExpect(jsonPath("$.timestamp").exists());
	}
	
	
	@Test
	public void login_shouldReturn200_SuccessLogin() throws Exception {
		
		
		//Arrange
		String email="lokeshjoshi@gmail.com";
		String password = "normalPassword";
		
		LoginRequest request = LoginRequest.builder()
				.email(email)
				.password(password)
				.build();
		AuthResponse response = AuthResponse.builder()
				.accessToken("fake-access-token")
				.refreshToken("fake-refresh-token")
				.build();
		
		
		//act
		when(authService.login(any(LoginRequest.class))).thenReturn(response);
		
		
		//assert
		
		mockMvc.perform(
				post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.accessToken").value("fake-access-token"))
		.andExpect(jsonPath("$.refreshToken").value("fake-refresh-token"));
		
	}
	
	
	@Test
	public void login_shouldReturnError_whenCredentialsAreInvalid() throws Exception {
		//Arrange
		String invalidEmail="lokeshjoshi@gmail.com";
		String invalidPassword = "normalPassword";
		
		
		LoginRequest request = LoginRequest.builder()
						.email(invalidEmail)
						.password(invalidPassword)
						.build();
		
		
		when(authService.login(any(LoginRequest.class))).thenThrow(new BadCredentialsException("") );
		
		
		
		mockMvc.perform(
				post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				
				)
		.andExpect(status().isUnauthorized()) 
		.andExpect(jsonPath("$.message").value("INVALID EMAIL OR PASSWORD"))
		.andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
		.andExpect(jsonPath("$.timestamp").exists())
		;	
	}
	

	
	
	
}

	
	
