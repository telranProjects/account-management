package telran.security.accounting.dto;

import java.util.Objects;

public record UsernameRoles(String username, Roles[] roles) {

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsernameRoles other = (UsernameRoles) obj;
		return Objects.equals(username, other.username);
	}

}
