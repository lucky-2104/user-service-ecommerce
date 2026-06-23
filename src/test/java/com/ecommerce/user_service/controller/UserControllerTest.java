package com.ecommerce.user_service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.ecommerce.user_service.config.SecurityConfig;
import com.ecommerce.user_service.security.JwtService;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {
	
	
	@MockBean
	private JwtService jwtService;
	
	
	@Autowired
	private MockMvc mockMvc;
	
	//Test 1 Admin Succeeds 202
	
	@Test
	@WithMockUser(roles="ADMIN")
	public void adminEndPoint_shouldReturn202_forAdminUser() throws Exception {
		
        mockMvc.perform(get("/api/v1/admin/test"))
        .andExpect(status().isAccepted());
	}
	
	
	
	
	//Test 2 Customer Denied 403
	@Test
	@WithMockUser(roles="CUSTOMER")
	public void adminEndPoint_shouldReturn403_forCustomerUser() throws Exception {
		
        mockMvc.perform(get("/api/v1/admin/test"))
        .andExpect(status().isForbidden());
	}
	
	
	
	

}
