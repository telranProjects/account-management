package telran.security.accounting.test;


import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.security.accounting.dto.AccountData;
import telran.security.accounting.dto.UsernameRoles;
import telran.security.accounting.dto.Roles;
import telran.security.accounting.model.AccountDataDoc;
import telran.security.accounting.repo.AccountRepo;
import telran.security.accounting.service.AccountService;
import telran.security.exception.AlreadyExistException;
import telran.security.exception.NotFoundException;

@SpringBootTest
class AccountServiceTest {
	
	@Autowired
	AccountRepo accountRepo;
	@Autowired
	AccountService accountService;
	
	String name = "name@name";
	String password = "password";
	Roles[] roles = new Roles[] { Roles.USER };
	
	String name2 = "name2@name";
	String password2 = "password2";
	Roles[] roles2 = new Roles[] { Roles.ADMIN, Roles.USER };

	AccountData accountData = new AccountData(name, password, roles);
	AccountData accountData2 = new AccountData(name2, password2, roles2);
	AccountDataDoc accountDataDoc = AccountDataDoc
			.builder()
			.username(name)
			.hashPassword(password)
			.roles(roles).build();
	
	AccountDataDoc accountDataDoc2 = AccountDataDoc
			.builder()
			.username(name2)
			.hashPassword(password2)
			.roles(roles2).build();
	UsernameRoles usernameRoles = new UsernameRoles(name, roles);
	UsernameRoles usernameRoles2 = new UsernameRoles(name2, roles2);
	
	List<AccountData> listBOaccountData = List.of(accountData, accountData2);
	List<AccountDataDoc> listBOaccountDataDoc = List.of(accountDataDoc, accountDataDoc2);
	List<UsernameRoles> listUsernameRoles = List.of(usernameRoles, usernameRoles2);

	@BeforeEach
	void setup() {
		accountRepo.deleteAll();
		accountRepo.save(accountDataDoc);
	}
	
	@Test
	void addUser_Success() {
		UsernameRoles usernameRoleActual = accountService.addAccount(accountData2);
		List<AccountDataDoc> actual = accountRepo.findAll();
		assertEquals(usernameRoles2, usernameRoleActual);
		assertIterableEquals(listBOaccountDataDoc, actual);
	}
	
	@Test
	void addUser_Except() {
		assertThrows(AlreadyExistException.class, 
				() -> accountService.addAccount(accountData));
		
	}
	
	@Test
	void deleteUser_Success() {
		UsernameRoles usernameRoleActual = accountService.deleteAccount(name);
		
		assertTrue(accountRepo.findAll().isEmpty());		
		assertEquals(usernameRoles, usernameRoleActual);
	}
	
	@Test
	void deleteUser_Except() {
		assertThrows(NotFoundException.class, () -> accountService.deleteAccount(name2));
	}
	
	@Test
	void getAllUsers_Test() {
		accountService.addAccount(accountData2);
		List<UsernameRoles> actual = accountService.getListUsernamesRoles();
		assertIterableEquals(listUsernameRoles, actual);
	}

}




































