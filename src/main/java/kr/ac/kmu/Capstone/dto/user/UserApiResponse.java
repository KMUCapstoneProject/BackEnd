package kr.ac.kmu.Capstone.dto.user;

import kr.ac.kmu.Capstone.entity.User;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class UserApiResponse {

	private Long id;
	private String email;
	private String nickname;

	@Builder
	public UserApiResponse(Long id, String email, String nickname) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
	}

	public static UserApiResponse of(User user) {
		return new UserApiResponse(user.getId(), user.getEmail(), user.getNickname());
	}

}
