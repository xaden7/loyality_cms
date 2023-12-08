package md.akdev.loyality_cms.config;

import md.akdev.loyality_cms.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class JwtSecurityConfig {
 private final JwtFilter jwtFilter;

    @Autowired
    public JwtSecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers( new AntPathRequestMatcher("/auth/token/**")).permitAll()
                                .requestMatchers( new AntPathRequestMatcher("/auth/login/**")).permitAll()
                                .requestMatchers( new AntPathRequestMatcher("/api/sms/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/promotions/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/branches/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/tags/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/device/**")).permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
