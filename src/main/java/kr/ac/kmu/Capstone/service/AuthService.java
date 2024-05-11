package kr.ac.kmu.Capstone.service;

import kr.ac.kmu.Capstone.config.common.response.ErrorCode;
import kr.ac.kmu.Capstone.config.jwt.TokenProvider;
import kr.ac.kmu.Capstone.dto.auth.*;
import kr.ac.kmu.Capstone.entity.RefreshToken;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.repository.RefreshTokenRepository;
import kr.ac.kmu.Capstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public void signup(UserSignUpTarget userSignUpTarget) {

        if (userRepository.existsByEmail(userSignUpTarget.getEmail())) {
            throw new DuplicateKeyException(ErrorCode.ID_ALREADY_EXIST.getMessage());
        }

        if (userRepository.existsByNickname(userSignUpTarget.getNickname())) {
            throw new DuplicateKeyException(ErrorCode.NICKNAME_ALREADY_EXIST.getMessage());
        }

        User user = userSignUpTarget.toEntity(passwordEncoder);
        userRepository.save(user);
    }

    public AuthApiResponse login(UserLoginTarget userLoginTarget) {

        User user = userRepository.findByEmail(userLoginTarget.getEmail())
            .orElseThrow(() -> new NullPointerException(ErrorCode.ID_NOT_EXIST.getMessage()));
        //user.changeDeviceToken(userLoginTarget.getDeviceToken());

        log.info("whereistheprob1");

        UsernamePasswordAuthenticationToken authenticationToken = userLoginTarget.toAuthentication();
        log.info("1");
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("2");
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        log.info("whereistheprob2");

        RefreshToken refreshToken = RefreshToken.builder()
            .refreshKey(authentication.getName())
            .refreshValue(tokenDto.getRefreshToken())
            .build();

        refreshTokenRepository.save(refreshToken);
        return AuthApiResponse.of(user,tokenDto);
    }

    public AuthApiResponse reissue(TokenRequestDto tokenRequestDto) {

        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException(ErrorCode.REFRESH_TOKEN_IS_NOT_VALID.getMessage());
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        User user = userRepository.findById(Long.parseLong(authentication.getName()))
            .orElseThrow(() -> new NullPointerException(ErrorCode.ID_NOT_EXIST.getMessage()));
        RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName())
            .orElseThrow(() -> new RuntimeException(ErrorCode.ALREADY_LOGOUT.getMessage()));

        if (!refreshToken.getRefreshValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException(ErrorCode.USER_INFO_NOT_MATCH.getMessage());
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);


        return AuthApiResponse.of(user,tokenDto);
    }


    public boolean checkDuplicateNickname(String nickname){
        return userRepository.existsByNickname(nickname);
    }


    public boolean checkDuplicateEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
