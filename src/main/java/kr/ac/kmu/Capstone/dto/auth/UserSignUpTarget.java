package kr.ac.kmu.Capstone.dto.auth;


import kr.ac.kmu.Capstone.entity.Role;
import kr.ac.kmu.Capstone.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignUpTarget {

	private String email;
	private String password;
	private String nickname;
	private Role authority;

	public User toEntity(PasswordEncoder passwordEncoder) {
		return User.builder()
			.email(email)
			.password(passwordEncoder.encode(password))
			.nickname(nickname)
			.roles(Role.USER)
			.build();
	}

}
