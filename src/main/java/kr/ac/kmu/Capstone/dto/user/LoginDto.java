package kr.ac.kmu.Capstone.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class LoginDto {

    @Email(message = "이메일 형식으로 입력하세요")
    private String email;

    private String password;


}
