package com.handong.chat.config;

import com.handong.chat.domain.user.UserEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // TODO: Add Jwt filter

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("filter chain successfully enrolled.");

        http.headers(header -> header
                .frameOptions(frameOption -> frameOption.disable())); // No permit iframe.
        http.csrf(csrf -> csrf.disable()); // for using POST MAN application.
        http.cors(cors -> cors.configurationSource(configurationSource())); // cors: Manage Javascript request permissions.

        // Our server not managing SessionId
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // Session form login disable
        http.formLogin(form -> form.disable());
        // httpBasic: allows the browser to authenticate using a pop-up window. (disable)
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Exceptions
        http.exceptionHandling(e -> e.authenticationEntryPoint((request, response, authException) -> {
            response.setStatus(403);
            response.getWriter().println("error");
        }));

        // https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
        http.authorizeRequests()
                .requestMatchers("/api/user/**").authenticated()
                .requestMatchers("/api/admin/**").hasRole("" + UserEnum.ADMIN)
                .anyRequest().permitAll();

        return http.build();
    }

    private CorsConfigurationSource configurationSource() {
        // CORS configuration
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Permit Javascript request)
        configuration.addAllowedOriginPattern("*"); // All IP address is possible
        configuration.setAllowCredentials(true); // Permit the client's cookies request
        configuration.addExposedHeader("Authorization"); // Allow the header's field name, then client can access the header value.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
