package kr.ac.kmu.Capstone.dto.user;

import kr.ac.kmu.Capstone.entity.Role;
import kr.ac.kmu.Capstone.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private long id;
    private String email;
    private String nickname;
    private Role authority;

    public static UserResponseDto of(User user) {
        return new UserResponseDto(user.getId(), user.getEmail(), user.getNickname(), user.getRoles());
    }
}
