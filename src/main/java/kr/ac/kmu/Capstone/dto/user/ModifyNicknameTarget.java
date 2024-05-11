package kr.ac.kmu.Capstone.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyNicknameTarget {

	private String email;
	private String nickname;


}
