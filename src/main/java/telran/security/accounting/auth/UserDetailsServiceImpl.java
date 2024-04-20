package telran.security.accounting.auth;

import java.util.Arrays;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.security.accounting.model.AccountDataDoc;
import telran.security.accounting.repo.AccountRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
	final AccountRepo accountRepo;

	@Override
	public UserDetails loadUserByUsername(String username) {
		AccountDataDoc user = accountRepo.findById(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("user %s not found ", username)));
		String[] roles = Arrays.stream(user.getRoles())
				.map(r -> "ROLE_" + r).toArray(String[]::new);
		log.debug("username: {}, roles: {}", user.getUsername(), Arrays.deepToString(roles));
		
		return new User(user.getUsername(), user.getHashPassword(), AuthorityUtils.createAuthorityList(roles));
	}

}
