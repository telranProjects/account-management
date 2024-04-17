package telran.security.accounting.test;

import static telran.security.accounting.dto.ErrorMessages.*;

import java.util.ArrayList;
import java.util.List;

import telran.security.exception.AlreadyExistException;
import telran.security.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.security.accounting.dto.AccountData;
import telran.security.accounting.dto.Roles;
import telran.security.accounting.service.AccountService;
import telran.security.accounting.dto.UsernameRoles;

@WebMvcTest
public class AccountControllerTest {
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	MockMvc mockMvc;
	@MockBean
	AccountService userService;

	@MockBean
	UserDetailsService UDService;
	@Autowired
	ObjectMapper mapper;

	@Value("${app.security.user.list.url}")
	String getAllUrl;
	@Value("${app.security.user.c.d.url}")
	String userUrl;

	private static final String name = "name@name";
	private static final String password = "password";
	private static final Roles[] roles = new Roles[] { Roles.USER };
	
	private static final String name2 = "name2@name";
	private static final String password2 = "password2";
	private static final Roles[] roles2 = new Roles[] { Roles.ADMIN, Roles.USER };

	private static final AccountData accountData = new AccountData(name, password, roles);
	private static final AccountData accountData2 = new AccountData(name2, password2, roles2);
	private static final UsernameRoles usernameRoles = new UsernameRoles(name, roles);
	private static final UsernameRoles usernameRoles2 = new UsernameRoles(name2, roles2);
	
	List<AccountData> listBOaccountData = List.of(accountData, accountData2);
	
	@SuppressWarnings("serial")
	List<UsernameRoles> listAccountnameRoles = new ArrayList<> () {
		{
		add(usernameRoles);
		add(usernameRoles2);
		}
	};

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void addUser_Success() throws Exception {
		when(userService.addAccount(accountData)).thenReturn(usernameRoles);
		String accountDataJSON = mapper.writeValueAsString(accountData);

		String response = mockMvc.perform(post(userUrl).contentType(MediaType.APPLICATION_JSON).content(accountDataJSON))
				.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		UsernameRoles actual = mapper.readValue(response, UsernameRoles.class);

		assertEquals(usernameRoles, actual);

	}

	@Test
	void addUser_Except() throws Exception {
		RuntimeException exception = new AlreadyExistException(USER_ALREADY_EXIST);

		when(userService.addAccount(accountData)).thenThrow(exception);
		String accountDataJSON = mapper.writeValueAsString(accountData);

		String response = mockMvc.perform(post(userUrl).contentType(MediaType.APPLICATION_JSON).content(accountDataJSON))
				.andDo(print()).andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();

		assertEquals(USER_ALREADY_EXIST, response);

	}

	@Test
	void deleteUser_Success() throws Exception {

		when(userService.deleteAccount(name)).thenReturn(usernameRoles);
		String fullUrl = userUrl + "/" + name;

		String response = mockMvc.perform(delete(fullUrl)).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		UsernameRoles actual = mapper.readValue(response, UsernameRoles.class);

		assertEquals(usernameRoles, actual);
	}

	@Test
	void deleteUser_Except() throws Exception {

		when(userService.deleteAccount(name)).thenThrow(new NotFoundException(USER_NOT_FOUND));
		String fullUrl = userUrl + "/" + name;

		String response = mockMvc.perform(delete(fullUrl)).andDo(print()).andExpect(status().is4xxClientError())
				.andReturn().getResponse().getContentAsString();

		assertEquals(USER_NOT_FOUND, response);
	}
	
	@Test
	void getAllUsers_Test() throws Exception {
		
		when(userService.getListUsernamesRoles()).thenReturn(listAccountnameRoles);
		String response = mockMvc.perform(get(getAllUrl)).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	
		List<UsernameRoles> actual = mapper.readValue(response, new TypeReference<List<UsernameRoles>>() {});
		
		assertIterableEquals(listAccountnameRoles, actual);
		
	
	}

}

























