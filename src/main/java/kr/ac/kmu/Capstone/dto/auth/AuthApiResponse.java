package kr.ac.kmu.Capstone.dto.auth;

import kr.ac.kmu.Capstone.entity.User;
import lombok.*;


@Getter
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class AuthApiResponse {

  private Long id;
  private String email;
  private String nickname;
  private String grantType;
  private String accessToken;
  private String refreshToken;
  private Long accessTokenExpiresIn;

  @Builder
  public AuthApiResponse(long id, String email, String nickname,
                         String grantType, String accessToken, String refreshToken, Long accessTokenExpiresIn) {
    this.id = id;
    this.email = email;
    this.nickname = nickname;
    this.grantType = grantType;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.accessTokenExpiresIn = accessTokenExpiresIn;
  }

  public static AuthApiResponse of(User user, TokenDto tokenDto) {
    return new AuthApiResponse(user.getId(),user.getEmail(),user.getNickname(), tokenDto.getGrantType(), tokenDto.getAccessToken(),
            tokenDto.getRefreshToken(), tokenDto.getAccessTokenExpiresIn());
  }

}
