package kr.ac.kmu.Capstone.config.auth;

import kr.ac.kmu.Capstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServices implements UserDetailsService {

    private final UserRepository userRepository;

    /** username이 DB에 존재하는지 확인 **/
    // username -> email
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /** 시큐리티 세션에 유저 정보 저장**/
        return new CustomUserDetails(userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("사용자가 존재하지 않습니다.")));
    }

}
