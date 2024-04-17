package telran.security.accounting.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.security.accounting.dto.AccountData;
import telran.security.accounting.dto.UsernameRoles;
import telran.security.accounting.model.AccountDataDoc;
import telran.security.accounting.repo.AccountRepo;
import telran.security.exception.AlreadyExistException;
import telran.security.exception.NotFoundException;

import static telran.security.accounting.dto.ErrorMessages.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
	final PasswordEncoder passwordEncoder;
	final AccountRepo accountRepo;

	@Override
	public List<UsernameRoles> getListUsernamesRoles() {
		List<UsernameRoles> res = null;
		List<AccountDataDoc> all = accountRepo.findAll();
		if (!all.isEmpty()) {
			res = all.stream().map(doc -> doc.buildUR()).toList();
		}
		log.trace("list users: {}", res);
		return res;
	}

	@Override
	public UsernameRoles addAccount(AccountData accountData) {
		if (accountRepo.existsById(accountData.username())) {
			throw new AlreadyExistException(USER_ALREADY_EXIST);
		}
		AccountDataDoc user = AccountDataDoc
				.builder()
				.username(accountData.username())
				.hashPassword(passwordEncoder.encode(accountData.password()))
				.roles(accountData.roles()).build();
		accountRepo.save(user);
		UsernameRoles res = user.buildUR();
		log.debug("user {} saved", res.username());
		return res;
	}

	@Override
	public UsernameRoles deleteAccount(String username) {
		AccountDataDoc user = accountRepo.findById(username).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
		UsernameRoles res = user.buildUR();
		accountRepo.deleteById(username);
		log.debug("user {} deleted", res);
		return res;
	}

}
