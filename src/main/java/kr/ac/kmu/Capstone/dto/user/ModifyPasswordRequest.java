package kr.ac.kmu.Capstone.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPasswordRequest {

	@NotBlank(message = "Input Your Password")
	private String password;

	public ModifyPasswordTarget toParam(String email) {
		return ModifyPasswordTarget.builder()
			.email(email)
			.password(password)
			.build();
	}


	/*public ModifyUserTarget toParam() {
		return ModifyUserTarget.builder()
			.password(password)
			.build();
	}*/







}
