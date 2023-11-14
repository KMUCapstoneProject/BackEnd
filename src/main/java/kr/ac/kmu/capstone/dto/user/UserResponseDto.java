package kr.ac.kmu.Capstone.dto.user;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String nickname;
    private String email;
    private String role;

/*    public User toEntity(Role roles){
        User user = User.builder()
                .id(id)
                .nickname(nickname)
                .email(email)
                .roles(roles)
                .build();
        return user;
    }*/

    @Builder
    public UserResponseDto(Long id, String nickname, String email, String role) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }

}
