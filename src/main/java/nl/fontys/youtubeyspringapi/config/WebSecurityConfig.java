package nl.fontys.youtubeyspringapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/login", "/register", "/").permitAll()
                                .requestMatchers("/api/requests/edits/**").authenticated()
                )
                .csrf(csrf -> csrf.disable()); // Disable CSRF for simplicity (you may want to enable it in a production environment)
                 // Disable frame options for H2 Console (if used)
        return http.build();
    }
}
