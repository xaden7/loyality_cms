package md.akdev.loyality_cms.config;

import md.akdev.loyality_cms.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                .httpBasic((basis) -> basis.disable())
                .csrf((csrf) -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers( new AntPathRequestMatcher("/user/token/**")).permitAll()
                                .requestMatchers( new AntPathRequestMatcher("/auth/login/**")).permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
