package kr.ac.kmu.Capstone.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServices customUserDetailsService;

    @Autowired
    public CustomAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsServices customUserDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String account = authentication.getName();
        String password = authentication.getCredentials().toString();

        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(account);

        if (passwordEncoder.matches(password, customUserDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
