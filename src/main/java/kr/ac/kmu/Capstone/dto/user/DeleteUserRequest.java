package kr.ac.kmu.Capstone.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserRequest {

	@NotBlank(message = "Input Your Password")
	private String password;

	public DeleteUserTarget toParam(String email) {
		return DeleteUserTarget.builder()
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
