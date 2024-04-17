package telran.security.accounting.model;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;
import telran.security.accounting.dto.UsernameRoles;
import telran.security.accounting.dto.Roles;

@AllArgsConstructor(access=AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Builder
@Document(collection = "user-password")
public class AccountDataDoc {
	@Id
	private String username;
    private String hashPassword;
    private Roles[] roles; 
    
    public UsernameRoles buildUR() {
    	return new UsernameRoles(username, roles);
    }

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
		AccountDataDoc other = (AccountDataDoc) obj;
		return Objects.equals(username, other.username);
	}

}
