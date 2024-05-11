package kr.ac.kmu.Capstone.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyPasswordTarget {

	private String email;
	private String password;

}
