package telran.security.accounting.dto;

import java.util.Objects;

import jakarta.validation.constraints.*;

public record AccountData(@NotEmpty @Email(message = "wrong email format") String username,
		@NotNull(message = "missing password") @Size(min = 4) String password,
		@NotEmpty(message = "roles are empty") Roles[] roles) {

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
		AccountData other = (AccountData) obj;
		return Objects.equals(username, other.username);
	}

}
