package kr.ac.kmu.Capstone.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ModifyNicknameRequest {

	@NotBlank(message = "Input Your Nickname")
	private String nickname;

	public ModifyNicknameTarget toParam(String email) {
		return ModifyNicknameTarget.builder()
			.email(email)
			.nickname(nickname)
			.build();
	}
}
