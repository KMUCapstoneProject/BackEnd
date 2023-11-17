package kr.ac.kmu.Capstone.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig{


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource());

        http
                .formLogin()// form data의 key value
                .loginProcessingUrl("/api/login") // usercontroller의 login 대신. 자동 생성
                .passwordParameter("password")
                .usernameParameter("email")
                .defaultSuccessUrl("/api/info", false);
        http
                .logout()
                .logoutUrl("/api/logout");
        http
                .authorizeRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                //.requestMatchers("/api/**").hasAnyRole("MANAGER", "USER")
                //.requestMatchers("/**").permitAll()
//                .requestMatchers("/api/**").permitAll()
                .anyRequest().permitAll();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // site address 수정하기?
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}