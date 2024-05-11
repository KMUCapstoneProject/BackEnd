package kr.ac.kmu.Capstone.dto.auth;


import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserLoginTarget {

    private String email;
    private String password;


//    public User toEntity(PasswordEncoder passwordEncoder) {
//        return User.builder()
//            .email(email)
//            .password(passwordEncoder.encode(password))
//            .roles(Role.USER)
//            .build();
//    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}