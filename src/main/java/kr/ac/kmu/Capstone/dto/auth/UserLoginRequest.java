package kr.ac.kmu.Capstone.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRequest {

    @NotBlank(message = "Input Your email")
    private String email;
    @NotBlank(message = "Input Your Password")
    private String password;


    public UserLoginTarget toParam() {
        return UserLoginTarget.builder()
            .email(email)
            .password(password)
            .build();
    }
}
