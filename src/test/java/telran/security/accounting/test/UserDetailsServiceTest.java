package telran.security.accounting.test;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import telran.security.accounting.dto.Roles;
import telran.security.accounting.model.AccountDataDoc;
import telran.security.accounting.repo.AccountRepo;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserDetailsServiceTest {	
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
    AccountRepo accountRepo;
	@Autowired
    UserDetailsService userDetailsService;
    
    String username = "test@user.com";
    String password = "password"; 
    Roles[] roles = {Roles.ADMIN, Roles.USER};
    
    @BeforeEach
    void setup() {
    	accountRepo.deleteAll();
    	AccountDataDoc user = AccountDataDoc
				.builder()
				.username(username)
				.hashPassword(passwordEncoder.encode(password))
				.roles(roles)
				.build();
    	accountRepo.save(user);    	
    }

    @Test
    void testLoadUserByUsername_ExistingUser() {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        assertEquals(username, userDetails.getUsername());
        assertEquals(2, userDetails.getAuthorities().size());
    }

    @Test
    void testLoadUserByUsername_NonExistingUser() {
        String username = "nonExistUser";
        assertThrowsExactly(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }        

    
}
