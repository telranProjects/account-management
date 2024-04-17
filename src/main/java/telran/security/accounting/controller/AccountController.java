package telran.security.accounting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.security.accounting.service.AccountService;
import telran.security.accounting.dto.AccountData;
import telran.security.accounting.dto.UsernameRoles;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountController {
	final AccountService accountService;
	
	@GetMapping("${app.security.user.list.url}")
	List<UsernameRoles> getListUsernamesRoles() {
		
		List<UsernameRoles> res = accountService.getListUsernamesRoles();
		log.trace("list users: {}", res);
		return res;
		
	}
	@PostMapping("${app.security.user.c.d.url}")
	UsernameRoles addAccount(@RequestBody @Valid AccountData accountData) {
		log.debug("user {} received for addition", accountData.username());
		UsernameRoles res = accountService.addAccount(accountData);
		log.debug("user {} successfully added", res.username());
		return res;
		
	}
	@DeleteMapping("${app.security.user.c.d.url}" + "/{username}")
	UsernameRoles deleteAccount(@PathVariable(name = "username") String username) {
		log.debug("username {} received for deletion", username);
		UsernameRoles res = accountService.deleteAccount(username);
		log.debug("user {} successfully deleted", res.username());
		return res;		
	}	
	
}