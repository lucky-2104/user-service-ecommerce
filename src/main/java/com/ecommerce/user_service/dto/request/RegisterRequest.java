package com.ecommerce.user_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//RegisterRequest.java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

	 @NotBlank(message = "Full name is required")
	 private String fullName;
	
	 @NotBlank(message = "Email is required")
	 @Email(message = "Invalid email format")
	 private String email;
	
	 @NotBlank(message = "Password is required")
	 @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
	 private String password;

 
}
