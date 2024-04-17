package telran.security.accounting.test;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import telran.security.accounting.dto.Roles;
import telran.security.accounting.dto.AccountData;
import telran.security.accounting.model.AccountDataDoc;
import telran.security.accounting.service.AccountService;


@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigurationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	AccountService accountService;
	
	@Value("${app.security.user.list.url}")
	private String userGetAllUrl;
	@Value("${app.security.user.c.d.url}")
	private String userCDUrl;
	
	String requestBody =  "{\"username\": \"username@username.il\", \"password\": \"password\", \"roles\": [\"ADMIN\", \"USER\"]}";
	
	String username = "username@username.il";
	String password = "password";
	Roles[] roles = new Roles[]{Roles.ADMIN, Roles.USER};
	AccountData accountData = new AccountData(username, password, roles);
	
	AccountDataDoc accountDoc = AccountDataDoc.builder()
			.username(username)
			.hashPassword(password)
			.roles(roles) 
			.build();

	@Test
	void testUserRole() throws Exception {
		
		String role = "USER";
		
		String url = userGetAllUrl;
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(url);		
		int status = getResponseStatus(request, role);
		assertEquals(HttpStatus.FORBIDDEN.value(), status);
		
		url = userGetAllUrl;
		request = MockMvcRequestBuilders.post(url);		
		status = getResponseStatus(request, role);
		assertEquals(HttpStatus.FORBIDDEN.value(), status);
		
		url = userGetAllUrl;
		request = MockMvcRequestBuilders.delete(url);		
		status = getResponseStatus(request, role);
		assertEquals(HttpStatus.FORBIDDEN.value(), status);		
		
		url = userCDUrl;
		request = MockMvcRequestBuilders.post(url);		
		status = getResponseStatus(request, role);
		assertEquals(HttpStatus.FORBIDDEN.value(), status);
		
		url = userCDUrl;
		request = MockMvcRequestBuilders.delete(url);		
		status = getResponseStatus(request, role);
		assertEquals(HttpStatus.FORBIDDEN.value(), status);
		
		url = userCDUrl;
		request = MockMvcRequestBuilders.get(url);		
		status = getResponseStatus(request, role);
		assertEquals(HttpStatus.FORBIDDEN.value(), status);

	}

	int getResponseStatus(MockHttpServletRequestBuilder request, String role) throws Exception {

		return mockMvc.perform(request
				.with(SecurityMockMvcRequestPostProcessors.user("user").roles(role))
				.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getStatus();
	}
	
	@Test
	void testAdminRole() throws Exception {
		
		String role = "ADMIN";
		
		String url = userCDUrl;
		when(accountService.addAccount(accountData)).thenReturn(accountDoc.buildUR());
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
		        .content(requestBody);
		int status = getResponseStatus(request, role);
		assertEquals(HttpStatus.FORBIDDEN.value(), status);		
		
		when(accountService.deleteAccount(username)).thenReturn(accountDoc.buildUR());
		request = MockMvcRequestBuilders.delete(url + "/" + username);
		status = getResponseStatus(request, role);
		assertEquals(HttpStatus.FORBIDDEN.value(), status);		
		
		
		url = userGetAllUrl;
		request = MockMvcRequestBuilders.get(url);		
		status = getResponseStatus(request, role);
		assertEquals(HttpStatus.FORBIDDEN.value(), status);
	}
	
	@Test
	void testSURole() throws Exception {
		
		String role = "SU";		
		
		String url = userCDUrl;
		when(accountService.addAccount(accountData)).thenReturn(accountDoc.buildUR());
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
		        .content(requestBody);
		int status = getResponseStatus(request, role);
		assertEquals(HttpStatus.OK.value(), status);
		
		when(accountService.deleteAccount(username)).thenReturn(accountDoc.buildUR());
		request = MockMvcRequestBuilders.delete(url + "/" + username);
		status = getResponseStatus(request, role);
		assertEquals(HttpStatus.OK.value(), status);		
		
		
		url = userGetAllUrl;
		request = MockMvcRequestBuilders.get(url);		
		status = getResponseStatus(request, role);
		assertEquals(HttpStatus.OK.value(), status);
	}

	
}

