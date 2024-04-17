package telran.security.accounting.service;

import java.util.List;

import telran.security.accounting.dto.AccountData;
import telran.security.accounting.dto.UsernameRoles;

public interface AccountService {
	List<UsernameRoles> getListUsernamesRoles();
	UsernameRoles addAccount(AccountData accountData);
	UsernameRoles deleteAccount(String usename);
}
