package kr.ac.kmu.Capstone.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignUpRequest {

	@NotBlank(message = "Input Your email")
	private String email;
	@NotBlank(message = "Input Your Password")
	private String password;
	@NotBlank(message = "Input Your Nickname")
	private String nickname;

	public UserSignUpTarget toParam() {
		return UserSignUpTarget.builder()
			.email(email)
			.password(password)
			.nickname(nickname)
			.build();
	}

	public UsernamePasswordAuthenticationToken toAuthentication() {
		return new UsernamePasswordAuthenticationToken(email, password);
	}
}
