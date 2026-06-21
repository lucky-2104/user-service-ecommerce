package com.ecommerce.user_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecommerce.user_service.dto.request.LoginRequest;
import com.ecommerce.user_service.dto.request.RegisterRequest;
import com.ecommerce.user_service.dto.response.AuthResponse;
import com.ecommerce.user_service.entity.Role;
import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.exception.UserAlreadyExistsException;
import com.ecommerce.user_service.repository.UserRepository;
import com.ecommerce.user_service.security.JwtService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private JwtService jwtService;
	
	@Mock
	private AuthenticationManager authenticationManager;
	
	@Mock
	private Authentication authentication;
	@InjectMocks
	private AuthService authService;
	
	
	//Mocks basically working on AAA which means Arrange , Act and Assert
	
	@Test
	void register_shouldCreateUser_WhenEmailDoesntExist() {
		
		
		//Arrange the necessary step to execute the code
		RegisterRequest req = new RegisterRequest("Lokesh Joshi","lokeshjoshi@gmail.com","normalPassword");
		
		User savedUser = User.builder().
				email("lokeshjoshi@gmail.com")
				.fullName("Lokesh Joshi")
				.role(Role.CUSTOMER)
				.password("hashedPassword")
				.build();
		
		when(userRepository.findByEmail("lokeshjoshi@gmail.com")).thenReturn(Optional.empty());
		
		when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
		
		when(userRepository.save(any(User.class))).thenReturn(savedUser);
		
		when(jwtService.generateAccessToken(any(User.class))).thenReturn("fake-access-token");
		
		when(jwtService.generateRefreshToken(any(User.class))).thenReturn("fake-refresh-token");
		
		//Act the action that needs to be performed
		AuthResponse response = authService.register(req);
		
		
		//Assert check if the authService is giving the result as it needed to be
		
		assertNotNull(response);
		assertEquals("fake-access-token",response.getAccessToken());
		assertEquals("fake-refresh-token",response.getRefreshToken());
		
		//Also checking verifying if the intented function have runned specific number of times.
		
		verify(userRepository,times(1)).save(any(User.class));
		verify(userRepository,times(1)).findByEmail(any(String.class));
		verify(passwordEncoder,times(1)).encode(any(String.class));
		verify(jwtService,times(1)).generateAccessToken(any(User.class));
		verify(jwtService,times(1)).generateRefreshToken(any(User.class));	
	}
	
	@Test
	void register_WhenEmailAlreadyExists() {
		//Arrange the necessary step to execute the code
		
		String userEmail = "lokeshjoshi@gmail.com";
		String userFullName = "Lokesh Joshi";
		String normalPassword = "normalPassword";

		RegisterRequest req = new RegisterRequest(userFullName,userEmail,normalPassword);
		User user = new User();  // Just fake user present from before.Can have values but just for checking purpose
		
		when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
		
		//Act+Assert
		
		assertThrows(UserAlreadyExistsException.class,()->{
			authService.register(req);
		});
		
		//verify
		verify(userRepository,never()).save(any(User.class));
		
		verify(userRepository,times(1)).findByEmail(userEmail);
		verify(passwordEncoder,never()).encode(normalPassword);
		verify(jwtService,never()).generateAccessToken(any(User.class));
		verify(jwtService,never()).generateRefreshToken(any(User.class));
	}
	
	@Test
	void login_shouldReturnToken_WhenCredentialsAreCorrect(){
		String userEmail = "lokeshjoshi@gmail.com";
		String password = "normalPassword";
		String fullName = "Lokesh Joshi";
		
		
		
		LoginRequest req = new LoginRequest(userEmail,password);
		
		User user = User.builder()
				.email(userEmail)
				.password(password)
				.role(Role.CUSTOMER)
				.fullName(fullName)
				.build();
		
		//Arrange
		//Mocking the authentication and then mocking the user from that authentication object;
		when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail,password))).thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(user);
		when(jwtService.generateAccessToken(user)).thenReturn("fake-access-token");
		when(jwtService.generateRefreshToken(user)).thenReturn("fake-refresh-token");
		
		//Act
		
		AuthResponse response= authService.login(req);
		
		//Assert
		assertNotNull(response);
		assertEquals("fake-access-token",response.getAccessToken());
		assertEquals("fake-refresh-token",response.getRefreshToken());
		
		//Verify
		verify(authenticationManager,times(1)).authenticate(new UsernamePasswordAuthenticationToken(userEmail,password));
		verify(authentication,times(1)).getPrincipal();
		verify(jwtService,times(1)).generateAccessToken(user);
		verify(jwtService,times(1)).generateRefreshToken(user);
		
		
	}
	
	@Test
	public void login_ThrowBadCredential_CreadentialsAreIncorrect() {
	String userEmail = "lokeshjoshi@gmail.com";
	String password = "normalPassword";
	String fullName = "Lokesh Joshi";
	
	
	
	LoginRequest req = new LoginRequest(userEmail,password);
	

	
	//Arrange
	//Mocking the authentication and then mocking the user from that authentication object;
	when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail,password))).thenThrow(new BadCredentialsException("Bad credentials"));

	
	//Assert + act

	assertThrows(BadCredentialsException.class , ()->{
		authService.login(req);
	});
	
	//Verify
	verify(authenticationManager,times(1)).authenticate(new UsernamePasswordAuthenticationToken(userEmail,password));
	verify(authentication,never()).getPrincipal();
	verify(jwtService,never()).generateAccessToken(any(User.class));
	verify(jwtService,never()).generateRefreshToken(any(User.class));
	
		
	}
	
	
}
